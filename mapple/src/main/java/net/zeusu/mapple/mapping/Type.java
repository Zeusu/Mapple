package net.zeusu.mapple.mapping;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.zeusu.mapple.annotations.Mapping;
import net.zeusu.mapple.utils.MappleUtils;

/**
 * @author Pierre "Zeusu" Grenier
 *
 */
public class Type implements Iterable<Property> {

    public static final class Link {

        protected final String sourceField;
        protected final String targetField;

        private Link(final String source, final String target) {
            sourceField = source;
            targetField = target;
        }

    }

    private final Class<?> origin;
    private final Map<String, Property> properties = new HashMap<>();
    private final List<Link> links;


    protected Type(final Class<?> cls) {
        origin = cls;
        links = new ArrayList<>();
        analyzeSource();
    }

    protected Type(final Class<?> cls, final List<Link> links) {
        origin = cls;
        this.links = links;
        analyzeTarget();
    }

    public Object newObj() {
        try {
            return origin.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException e) {
            throw new RuntimeException("Couldn't not create object from: " + origin.getName(), e);
        }
    }

    private void analyzeSource() {

        for (Field f : origin.getDeclaredFields()) {
            if (f.isAnnotationPresent(Mapping.class)) {
                treatField(f);
                String target = f.getName();
                final Mapping m = f.getAnnotation(Mapping.class);
                if (!m.value().isBlank()) {
                    target = m.value();
                }
                links.add(new Link(f.getName(), target));
            }
        }
    }

    private void analyzeTarget() {
        for (Link l : links) {
            try {
                treatField(origin.getDeclaredField(l.targetField));
            } catch (NoSuchFieldException | SecurityException e) {
                System.err.println("[Mapple] No such field: " + origin.getSimpleName() + "." + l.targetField);
            }
        }
    }

    private void treatField(final Field f) {
        Method set = null;
        Method get = null;

        try {
            set = origin.getMethod(MappleUtils.setter(f), f.getType());
        } catch (Throwable t) {
        }

        try {
            get = origin.getMethod(MappleUtils.getter(f));
        } catch (Throwable t) {
        }

        properties.put(f.getName(), new Property(f, set, get));
    }

    protected Property prop(final String name) {
        return properties.get(name);
    }


    @Override
    public Iterator<Property> iterator() {
        return properties.values().iterator();
    }

    protected List<Link> getLinks() {
        return links;
    }

    protected Class<?> getOrigin() {
        return origin;
    }


    @Override
    public String toString() {
        return origin.getSimpleName();
    }

}
