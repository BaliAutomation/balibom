package ac.bali.bom.supply.mouser.model;

import java.util.List;
import org.apache.polygene.api.common.Optional;
import org.apache.polygene.api.property.Property;

public interface SearchResponseRoot
{
    Property<List<ErrorEntity>> Errors();

    @Optional
    Property<SearchResponse> SearchResults();
}
