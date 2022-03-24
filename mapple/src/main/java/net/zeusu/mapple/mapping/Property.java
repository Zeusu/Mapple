package net.zeusu.mapple.mapping;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import net.zeusu.mapple.annotations.Mapping;
import net.zeusu.mapple.utils.MappleUtils;

/**
 * @author Pierre "Zeusu" Grenier
 *
 */
public final class Property {
    private final Field f;
    private final String name;
    private final Method setter;
    private final Method getter;

    protected Property(final Field f, final Method setter, final Method getter) {
        this.f = f;
        name = f.getName();
        this.setter = setter;
        this.getter = getter;
    }

    public void set(final Object origin, final Object arg) {
        try {
            setter.invoke(origin, arg);
        } catch (Exception e) {
        }
    }

    public Object get(final Object origin) {
        try {
            return getter.invoke(origin);
        } catch (Exception e) {
        }
        return null;
    }


    public Class<?> getType() {
        return f.getType();
    }

    public boolean isSimple() {
        return MappleUtils.isSimpleType(f.getType());
    }

    public String getName() {
        return name;
    }

    public Mapping getAnnotation() {
        return f.getAnnotation(Mapping.class);
    }

    @Override
    public String toString() {
        return f.getType().getSimpleName() + " " + name + " [" + (setter != null ? setter.getName() : "none") + " | "
                + (getter != null ? getter.getName() : "none") + "]";
    }

}
