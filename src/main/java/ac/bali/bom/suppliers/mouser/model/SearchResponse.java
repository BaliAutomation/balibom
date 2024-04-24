package ac.bali.bom.suppliers.mouser.model;

import java.util.List;
import org.apache.polygene.api.property.Property;

public interface SearchResponse
{
    Property<Integer> NumberOfResult();
    Property<List<MouserPart>> Parts();
}
