package ac.bali.bom.supply.mouser.model;

import java.util.List;
import org.apache.polygene.api.property.Property;

public interface MouserManufacturersNameRoot
{
    Property<MouserManufacturerNameList> MouserManufacturerList();
    Property<List<ErrorEntity>> Errors();
}
