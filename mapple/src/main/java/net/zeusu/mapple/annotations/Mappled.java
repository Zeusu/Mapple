package net.zeusu.mapple.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Pierre "Zeusu" Grenier
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Mappled {

    public Class<?> value();

    public Class<?> inherit() default Object.class;

}
