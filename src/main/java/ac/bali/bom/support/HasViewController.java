package ac.bali.bom.support;

import java.lang.annotation.*;

@Retention( RetentionPolicy.RUNTIME )
@Target( { ElementType.TYPE } )
@Documented
public @interface HasViewController
{
    Class<?>[] value();
}
