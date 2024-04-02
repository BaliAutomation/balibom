package ac.bali.bom.parts;

import ac.bali.bom.support.Order;
import ac.bali.bom.support.RenderAsName;
import org.apache.polygene.api.common.UseDefaults;
import org.apache.polygene.api.identity.HasIdentity;
import org.apache.polygene.api.property.Property;

import java.util.List;

public interface Manufacturer extends HasIdentity {

    @UseDefaults("")
    @Order(2)
    @RenderAsName
    Property<String> shortName();

    @UseDefaults("")
    @Order(3)
    @RenderAsName
    Property<String> fullName();

    @UseDefaults("")
    @Order(4)
    Property<String> country();

    @UseDefaults
    @Order(5)
    Property<List<String>> alternateNames();
}
