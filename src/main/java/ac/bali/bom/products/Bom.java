package ac.bali.bom.products;

import ac.bali.bom.ui.support.Order;
import ac.bali.bom.ui.support.RenderAsName;
import org.apache.polygene.api.common.UseDefaults;
import org.apache.polygene.api.property.Property;

import java.util.List;

public interface Bom
{
    @RenderAsName
    @Order(1)
    Property<String> product();

    @RenderAsName
    @Order(2)
    Property<String> revision();

    @Order(3)
    Property<List<BomItem>> items();

    @UseDefaults
    Property<List<String>> errors();
}
