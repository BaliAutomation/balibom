package ac.bali.bom.supply.mouser.model;

import org.apache.polygene.api.property.Property;

public interface SearchByPartMfrNameRequest
{
    Property<String> manufacturerName();
    Property<String> mouserPartNumber();
    Property<String> partSearchOptions();
}
