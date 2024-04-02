package ac.bali.bom.support;

import javafx.scene.control.ListCell;

import java.lang.annotation.*;

@Retention( RetentionPolicy.RUNTIME )
@Target( { ElementType.TYPE } )
@Documented
public @interface ListRenderer
{
    Class<? extends ListCell<?>> value();
}
