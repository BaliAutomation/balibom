package ac.bali.bom.order;

import ac.bali.bom.suppliers.Supplier;
import java.time.LocalDate;
import java.util.List;
import org.apache.polygene.api.association.Association;
import org.apache.polygene.api.identity.HasIdentity;
import org.apache.polygene.api.property.Property;

@SuppressWarnings("unused")
public interface Order extends HasIdentity
{
    Association<Supplier> supplier();
    Property<LocalDate> orderTime();
    Property<List<LocalDate>> fulfilledTimes();
    Property<LocalDate> receivedTime();
    Property<List<OrderItem>> items();
}
