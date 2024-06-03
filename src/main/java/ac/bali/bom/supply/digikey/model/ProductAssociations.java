package ac.bali.bom.supply.digikey.model;

import java.util.List;
import org.apache.polygene.api.property.Property;

@SuppressWarnings("unused")
public interface ProductAssociations
{
    Property<List<ProductSummary>> Kits();

    Property<List<ProductSummary>> MatingProducts();

    Property<List<ProductSummary>> AssociatedProducts();

    Property<List<ProductSummary>> ForUseWithProducts();
}
