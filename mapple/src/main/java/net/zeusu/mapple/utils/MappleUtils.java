package net.zeusu.mapple.utils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * @author Pierre "Zeusu" Grenier
 *
 */
public final class MappleUtils {

    private static final Class<?>[] PRIMALS = { int.class, Integer.class, long.class, Long.class, short.class,
            Short.class, byte.class, Byte.class, float.class, Float.class, double.class, Double.class, char.class,
            Character.class, boolean.class, Boolean.class, String.class, UUID.class };

    private static final Set<Class<?>> SIMPLE_TYPES = new HashSet<>(Arrays.asList(PRIMALS));

    public static boolean isSimpleType(final Class<?> cls) {
        return SIMPLE_TYPES.contains(cls) || cls.isEnum();
    }

    public static void addSimpleType(final Class<?> cls) {
        SIMPLE_TYPES.add(cls);
    }

    public static String setter(final Field f) {
        return "set" + majName(f);
    }

    public static String getter(final Field f) {
        final String getterPrefix = f.getType() != boolean.class ? "get" : "is";
        return getterPrefix + majName(f);
    }

    private static String majName(final Field f) {
        final String name = f.getName();
        return Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }



}
