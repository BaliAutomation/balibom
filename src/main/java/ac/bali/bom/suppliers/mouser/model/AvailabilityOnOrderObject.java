package ac.bali.bom.suppliers.mouser.model;

import java.time.LocalDateTime;
import org.apache.polygene.api.property.Property;

public interface AvailabilityOnOrderObject
{
    Property<Integer> Quantity();
    Property<LocalDateTime> Date();
}
