package ac.bali.bom.supply.order;

import ac.bali.bom.supply.Supplier;
import ac.bali.bom.view.AssociationFormatter;
import ac.bali.bom.view.Deleter;
import java.time.LocalDate;
import java.util.List;
import org.apache.polygene.api.association.Association;
import org.apache.polygene.api.identity.HasIdentity;
import org.apache.polygene.api.property.Property;
import org.qi4j.library.crudui.HasListViewController;
import org.qi4j.library.crudui.Order;
import org.qi4j.library.crudui.RenderAsName;

@SuppressWarnings("unused")
@HasListViewController({SupplierOrderService.class, Deleter.class})
public interface SupplierOrder extends HasIdentity
{
    @RenderAsName(format = AssociationFormatter.class )
    @Order(10)
    Association<Supplier> supplier();

    @Order(20)
    Property<LocalDate> orderTime();

    @Order(30)
    Property<LocalDate> receivedTime();

    @Order(40)
    Property<List<LocalDate>> fulfilledTimes();

    @Order(50)
    Property<List<SupplierOrderItem>> items();
}
