package ac.bali.bom.ui.support;

import java.lang.annotation.*;

@Retention( RetentionPolicy.RUNTIME )
@Target( { ElementType.METHOD } )
@Documented
public @interface Order
{
    int value() default -1;
}
