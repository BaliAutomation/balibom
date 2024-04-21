package ac.bali.bom.manufacturers;

import org.qi4j.library.javafx.support.Order;
import org.qi4j.library.javafx.support.RenderAsName;
import java.util.List;
import org.apache.polygene.api.common.UseDefaults;
import org.apache.polygene.api.identity.HasIdentity;
import org.apache.polygene.api.property.Property;

public interface Manufacturer extends HasIdentity {

    @UseDefaults
    @Order(2)
    @RenderAsName
    Property<String> identifier();

    @UseDefaults
    @Order(3)
    Property<String> fullName();

    @UseDefaults
    @Order(4)
    Property<String> country();

    @UseDefaults
    @Order(5)
    Property<List<String>> alternateNames();
}
