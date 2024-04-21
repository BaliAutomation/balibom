package ac.bali.bom.suppliers.digikey.model;

import java.util.List;
import org.apache.polygene.api.property.Property;

@SuppressWarnings("unused")
public interface FilterOptions
{
    Property<List<BaseFilterV4>> Manufacturers();
    Property<List<BaseFilterV4>> Packaging();
    Property<List<BaseFilterV4>> Status();
    Property<List<BaseFilterV4>> Series();
    Property<List<ParameterFilterOptionsResponse>> ParametricFilters();
    Property<List<TopCategory>> TopCategories();
    Property<List<MarketPlaceFiltersEnum>> MarketPlaceFilters();
}
