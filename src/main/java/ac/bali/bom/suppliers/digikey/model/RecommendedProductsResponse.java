package ac.bali.bom.suppliers.digikey.model;

import java.util.List;
import org.apache.polygene.api.property.Property;

@SuppressWarnings("unused")
public interface RecommendedProductsResponse
{
    Property<List<Recommendation>> Recommendations();
}
