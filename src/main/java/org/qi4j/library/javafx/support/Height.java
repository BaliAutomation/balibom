package org.qi4j.library.javafx.support;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention( RetentionPolicy.RUNTIME )
@Target( { ElementType.METHOD } )
@Documented
public @interface Height
{
    int pref() default -1;
    int min() default -1;
    int max() default -1;
}
