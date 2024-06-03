package org.qi4j.library.crudui;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.function.Function;

@Retention( RetentionPolicy.RUNTIME )
@Target( { ElementType.METHOD } )
@Documented
public @interface RenderAsName
{
    Class<? extends Function<Object,String>> format() default ToStringFormatter.class;
}
