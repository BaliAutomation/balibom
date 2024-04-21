package ac.bali.bom.suppliers.digikey.model;

import java.util.List;
import org.apache.polygene.api.property.Property;

@SuppressWarnings("unused")
public interface CategoriesResponse
{
    Property<Integer> ProductCount();
    Property<List<Category>> Categories();
    Property<IsoSearchLocale> SearchLocaleUsed();
}
