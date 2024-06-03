package org.qi4j.library.crudui;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.apache.polygene.api.identity.Identity;
import org.apache.polygene.api.property.Immutable;
import org.apache.polygene.api.property.Property;

@Retention( RetentionPolicy.RUNTIME )
@Target( { ElementType.METHOD } )
@Documented
public @interface Order
{
    int value() default -1;
}
