package ac.bali.bom.suppliers;

import ac.bali.bom.ui.support.Order;
import ac.bali.bom.ui.support.RenderAsName;
import java.util.List;
import org.apache.polygene.api.common.UseDefaults;
import org.apache.polygene.api.identity.HasIdentity;
import org.apache.polygene.api.property.Property;

public interface Supplier extends HasIdentity {

    @RenderAsName
    @Order(1)
    Property<String> name();

    @Order(2)
    Property<String> website();

    @Order(3)
    Property<String> searchApi();

    @Order(4)
    Property<String> productDetailsApi();

    @Order(5)
    Property<String> orderingApi();

    @UseDefaults
    Property<List<String>> bomColumns();

}
