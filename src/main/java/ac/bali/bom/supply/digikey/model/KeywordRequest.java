package ac.bali.bom.supply.digikey.model;

import org.apache.polygene.api.common.Optional;
import org.apache.polygene.api.property.Property;

@SuppressWarnings("unused")
public interface KeywordRequest
{
    Property<String> Keywords();

    Property<Integer> Limit();

    Property<Integer> Offset();

    @Optional
    Property<FilterOptionsRequest> FilterOptionsRequest();

    Property<SortOptions> SortOptions();

}
