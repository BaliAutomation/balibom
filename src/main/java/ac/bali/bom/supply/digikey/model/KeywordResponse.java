package ac.bali.bom.supply.digikey.model;

import java.util.List;
import org.apache.polygene.api.common.Optional;
import org.apache.polygene.api.property.Property;

@SuppressWarnings("unused")
public interface KeywordResponse
{
    Property<List<Product>> Products();

    Property<Integer> ProductsCount();

    Property<List<Product>> ExactMatches();

    @Optional
    Property<FilterOptions> FilterOptions();

    Property<IsoSearchLocale> SearchLocaleUsed();

    @Optional
    Property<List<Parameter>> AppliedParametricFiltersDto();
}
