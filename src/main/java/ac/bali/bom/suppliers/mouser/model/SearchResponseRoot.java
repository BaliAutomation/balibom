package ac.bali.bom.suppliers.mouser.model;

import java.util.List;
import org.apache.polygene.api.property.Property;

public interface SearchResponseRoot
{
    Property<List<ErrorEntity>> Errors();
    Property<SearchResponse> SearchResults();
}
