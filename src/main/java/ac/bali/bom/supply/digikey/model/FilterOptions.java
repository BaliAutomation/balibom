package ac.bali.bom.supply.digikey.model;

import java.util.List;
import org.apache.polygene.api.common.Optional;
import org.apache.polygene.api.property.Property;

@SuppressWarnings("unused")
public interface FilterOptions
{
    Property<List<BaseFilterV4>> Manufacturers();

    @Optional
    Property<List<BaseFilterV4>> Packaging();

    @Optional
    Property<List<BaseFilterV4>> Status();

    @Optional
    Property<List<BaseFilterV4>> Series();

    @Optional
    Property<List<ParameterFilterOptionsResponse>> ParametricFilters();

    Property<List<TopCategory>> TopCategories();

    Property<List<MarketPlaceFiltersEnum>> MarketPlaceFilters();
}
