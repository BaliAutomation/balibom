package ac.bali.bom.suppliers.mouser.model;

import java.util.List;
import org.apache.polygene.api.property.Property;

public interface MouserManufacturerNameList
{
    Property<Integer> Count();
    Property<List<MouserManufacturerName>> ManufacturerList();
}
