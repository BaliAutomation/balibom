package ac.bali.bom.support;

import java.lang.annotation.*;

@Retention( RetentionPolicy.RUNTIME )
@Target( { ElementType.METHOD } )
@Documented
public @interface Action
{
    String label();
    String description() default "";
    ActionScope scope() default ActionScope.type;
}
