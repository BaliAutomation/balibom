package ac.bali.bom.suppliers.mouser.model;

import java.time.LocalDate;
import org.apache.polygene.api.property.Property;

public interface AvailabilityOnOrderObject
{
    Property<Integer> Quantity();
    Property<LocalDate> Date();
}
