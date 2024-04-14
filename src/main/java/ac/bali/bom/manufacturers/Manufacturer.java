package ac.bali.bom.manufacturers;

import ac.bali.bom.ui.support.Order;
import ac.bali.bom.ui.support.RenderAsName;
import org.apache.polygene.api.common.UseDefaults;
import org.apache.polygene.api.identity.HasIdentity;
import org.apache.polygene.api.property.Property;

import java.util.List;

public interface Manufacturer extends HasIdentity {

    @UseDefaults("")
    @Order(2)
    @RenderAsName
    Property<String> identifier();

    @UseDefaults("")
    @Order(3)
    Property<String> fullName();

    @UseDefaults("")
    @Order(4)
    Property<String> country();

    @UseDefaults
    @Order(5)
    Property<List<String>> alternateNames();
}
