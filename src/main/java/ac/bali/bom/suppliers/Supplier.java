package ac.bali.bom.suppliers;

import java.util.List;
import org.apache.polygene.api.common.UseDefaults;
import org.apache.polygene.api.identity.HasIdentity;
import org.apache.polygene.api.property.Property;
import org.qi4j.library.javafx.support.Order;
import org.qi4j.library.javafx.support.RenderAsName;

public interface Supplier extends HasIdentity {

    Supply searchSupplierPartNumber(String supplierPartNumber);

    Supply searchManufacturerPartNumber(String mf, String mpn);

    List<Supply> searchKeywords(String keywords);


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
