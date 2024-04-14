package ac.bali.bom.order;

import java.time.LocalDate;
import java.util.List;

import ac.bali.bom.suppliers.Supplier;
import org.apache.polygene.api.association.Association;
import org.apache.polygene.api.identity.HasIdentity;
import org.apache.polygene.api.property.Property;

public interface Order extends HasIdentity
{
    Association<Supplier> supplier();
    Property<LocalDate> orderTime();
    Property<List<LocalDate>> fulfilledTimes();
    Property<LocalDate> receivedTime();
    Property<List<OrderItem>> items();
}
