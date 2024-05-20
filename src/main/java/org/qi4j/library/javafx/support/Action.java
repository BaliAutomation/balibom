package org.qi4j.library.javafx.support;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention( RetentionPolicy.RUNTIME )
@Target( { ElementType.METHOD } )
@Documented
public @interface Action
{
    String label();

    @SuppressWarnings("unused")
    String description() default "";

    ActionScope scope() default ActionScope.type;

    boolean showResult() default false;
}
