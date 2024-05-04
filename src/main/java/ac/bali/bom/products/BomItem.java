package ac.bali.bom.products;

import java.util.List;
import org.qi4j.library.javafx.support.Order;
import org.qi4j.library.javafx.support.RenderAsName;
import java.util.Map;
import org.apache.polygene.api.common.UseDefaults;
import org.apache.polygene.api.property.Property;

public interface BomItem
{
    @RenderAsName
    @Order(1)
    Property<String> designator();

    @UseDefaults
    @Order(10)
    Property<String> mf();

    @UseDefaults
    @Order(15)
    Property<String> mpn();

    @UseDefaults
    @Order(5)
    Property<String> value();

    @UseDefaults
    @Order(20)
    Property<String> footprint();

    @UseDefaults
    @Order(7)
    Property<Integer> quantity();

    @UseDefaults
    @Order(30)
    Property<Map<String,String>> attributes();

    @UseDefaults
    Property<List<String>> errors();
}
