package net.zeusu.mapple.mapping;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.zeusu.mapple.Mapple;
import net.zeusu.mapple.annotations.Mappled;
import net.zeusu.mapple.mapping.Type.Link;
import net.zeusu.mapple.utils.MappleUtils;

/**
 * @author Pierre "Zeusu" Grenier
 *
 */
public class Mapper {


    private final Type source;
    private final Type target;

    private Mapper superMapper = null;

    public Mapper(final Class<?> source, final Class<?> target) {
        this.source = new Type(source);
        this.target = new Type(target, this.source.getLinks());
    }


    public Class<?> mapperFor() {
        return source.getClass();
    }

    public Object toSource(final Object t) {
        if (t == null) return null;
        Object s;
        try {
            s = source.newObj();
        } catch (Throwable th) {
            Mapper m = Mapple.getRevert(t.getClass());
            if (m == null || m == this) return null;
            return m.toSource(t);
        }
        return toSource(t, s);
    }


    @SuppressWarnings("unchecked")
    private Object toSource(final Object t, final Object s) {
        if (superMapper != null) superMapper.toSource(t, s);

        for (Link l : source.getLinks()) {
            final Property sp = source.prop(l.sourceField);
            final Property tp = target.prop(l.targetField);
            if (sp.isSimple()) {
                sp.set(s, tp.get(t));
            } else if (sp.getType() != Object.class && Collection.class.isAssignableFrom(sp.getType())) {

                Collection<Object> c;
                try {
                    c = (Collection<Object>) sp.getType().getConstructor().newInstance();
                } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
                        | IllegalArgumentException | InvocationTargetException e) {
                    if (List.class.isAssignableFrom(sp.getType())) {
                        c = new ArrayList<>();
                    } else if (Set.class.isAssignableFrom(sp.getType())) {
                        c = new HashSet<>();
                    } else {
                        throw new RuntimeException("This collection isn't supported");
                    }
                }

                mapCollection((Collection<Object>) tp.get(t), c, true);
                sp.set(s, c);

            } else if (Object.class.isAssignableFrom(sp.getType())) {
                Mapper m = Mapple.get(sp.getType());
                if (m != null) {
                    final Object or = m.toSource(tp.get(t));
                    sp.set(s, or);
                } else {
                    System.err.println("[Mapple] No mapper found for " + sp.getType().getName());
                }

            }
        }
        return s;
    }


    public Object toTarget(final Object s) {
        if (s == null) return null;
        Object t;
        try {
            t = target.newObj();
        } catch (Throwable th) {
            Mapper m = Mapple.get(s.getClass());
            if (m == null || m == this) return null;
            return m.toTarget(s);
        }
        return toTarget(s, t);
    }


    @SuppressWarnings("unchecked")
    private Object toTarget(final Object s, final Object t) {
        if (superMapper != null) superMapper.toTarget(s, t);

        for (Link l : source.getLinks()) {
            final Property sp = source.prop(l.sourceField);
            final Property tp = target.prop(l.targetField);
            if (sp.isSimple()) {
                tp.set(t, sp.get(s));
            } else if (sp.getType() != Object.class && Collection.class.isAssignableFrom(sp.getType())) {

                Collection<Object> c;
                try {
                    c = (Collection<Object>) sp.getType().getConstructor().newInstance();
                } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
                        | IllegalArgumentException | InvocationTargetException e) {
                    if (List.class.isAssignableFrom(sp.getType())) {
                        c = new ArrayList<>();
                    } else if (Set.class.isAssignableFrom(sp.getType())) {
                        c = new HashSet<>();
                    } else {
                        throw new RuntimeException("This collection isn't supported");
                    }
                }

                mapCollection((Collection<Object>) sp.get(s), c, false);
                tp.set(t, c);

            } else if (Object.class.isAssignableFrom(sp.getType())) {
                Mapper m = Mapple.get(sp.getType());
                if (m != null) {
                    tp.set(t, m.toTarget(sp.get(s)));
                } else {
                    System.err.println("[Mapple] No mapper found for " + sp.getType().getName());
                }

            }
        }
        return t;
    }

    private void mapCollection(final Collection<Object> c1, final Collection<Object> c2, final boolean toSource) {
        if (c1 == null) {
            return;
        }
        for (Object o : c1) {
            if (MappleUtils.isSimpleType(o.getClass())) c2.add(o);
            else {
                Mapper m = Mapple.get(o.getClass());
                if (m == null) {
                    m = Mapple.getRevert(o.getClass());
                }
                c2.add(toSource ? m.toSource(o) : m.toTarget(o));
            }
        }
    }


    @Override
    public String toString() {
        String ln = System.lineSeparator();
        String result = " =======[ " + source + " / " + target + " ]======= " + ln;
        if (superMapper != null) {
            result += " =>  Extends: [ " + superMapper.source + " / " + superMapper.target + " ]" + ln;
        }
        result += source.toString() + " {" + ln;
        for (Property p : source) {
            result += " + " + p + " (Simple: " + MappleUtils.isSimpleType(p.getType()) + ")" + ln;
        }
        result += "}" + ln + ln + target.toString() + " {" + ln;
        for (Property p : target) {
            result += " + " + p + " (Simple: " + MappleUtils.isSimpleType(p.getType()) + ")" + ln;
        }
        result += "}" + ln;
        return result;
    }

    public Class<?> inheritFrom() {
        Class<?> inheritance = source.getOrigin().getAnnotation(Mappled.class).inherit();
        return inheritance == Object.class ? null : inheritance;

    }

    public void setSupperMapper(final Mapper mapper) {
        superMapper = mapper;
    }



}
