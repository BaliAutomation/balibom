package ac.bali.bom.products;

import org.apache.polygene.api.association.ManyAssociation;
import org.apache.polygene.api.entity.Aggregated;
import org.qi4j.library.javafx.support.Order;
import org.qi4j.library.javafx.support.RenderAsName;
import java.util.List;
import org.apache.polygene.api.common.UseDefaults;
import org.apache.polygene.api.property.Property;

public interface Bom
{
    @RenderAsName
    @Order(1)
    Property<String> product();

    @RenderAsName
    @Order(2)
    Property<String> revision();

    @Order(3)
    @Aggregated
    ManyAssociation<BomItem> items();

    @UseDefaults
    Property<List<String>> errors();
}
