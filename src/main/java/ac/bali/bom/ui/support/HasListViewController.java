package ac.bali.bom.ui.support;

import java.lang.annotation.*;

@Retention( RetentionPolicy.RUNTIME )
@Target( { ElementType.TYPE } )
@Documented
public @interface HasListViewController
{
    Class<?>[] value();
}
