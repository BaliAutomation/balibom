package ac.bali.bom.suppliers.digikey.model;

import java.util.List;
import org.apache.polygene.api.common.Optional;
import org.apache.polygene.api.common.UseDefaults;
import org.apache.polygene.api.property.Property;

@SuppressWarnings("unused")
public interface FilterOptionsRequest
{
    @UseDefaults
    Property<List<FilterId>> ManufacturerFilter();

    @UseDefaults
    Property<List<FilterId>> CategoryFilter();

    @UseDefaults
    Property<List<FilterId>> StatusFilter();

    @UseDefaults
    Property<List<FilterId>> PackagingFilter();

    @UseDefaults("NoFilter")
    Property<MarketPlaceFilterEnum> MarketPlaceFilter();

    @UseDefaults
    Property<List<FilterId>> SeriesFilter();

    @UseDefaults
    Property<Integer> MinimumQuantityAvailable();

    @Optional
    Property<ParameterFilterRequest> ParameterFilterRequest();

    @UseDefaults
    Property<List<SearchOptionsEnum>> SearchOptions();
}
