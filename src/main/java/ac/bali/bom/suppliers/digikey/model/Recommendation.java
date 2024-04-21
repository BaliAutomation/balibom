package ac.bali.bom.suppliers.digikey.model;

import java.util.List;
import org.apache.polygene.api.property.Property;

@SuppressWarnings("unused")
public interface Recommendation
{
    Property<String> ProductNumber();
    Property<List<RecommendedProduct>> RecommendedProducts();
    Property<IsoSearchLocale> SearchLocaleUsed();
}
