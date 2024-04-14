package ac.bali.bom.ui.support;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javafx.scene.control.ListCell;

@Retention( RetentionPolicy.RUNTIME )
@Target( { ElementType.METHOD } )
@Documented
public @interface Height
{
    int value();
}
