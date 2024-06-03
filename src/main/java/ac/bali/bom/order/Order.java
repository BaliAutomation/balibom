package ac.bali.bom.order;

import ac.bali.bom.suppliers.Supplier;
import ac.bali.bom.view.AssociationFormatter;
import ac.bali.bom.view.Deleter;
import java.time.LocalDate;
import java.util.List;
import org.apache.polygene.api.association.Association;
import org.apache.polygene.api.identity.HasIdentity;
import org.apache.polygene.api.property.Property;
import org.qi4j.library.crudui.HasListViewController;
import org.qi4j.library.crudui.RenderAsName;

@SuppressWarnings("unused")
@HasListViewController({OrderService.class, Deleter.class})
public interface Order extends HasIdentity
{
    @RenderAsName(format = AssociationFormatter.class )
    @org.qi4j.library.crudui.Order(10)
    Association<Supplier> supplier();

    @org.qi4j.library.crudui.Order(20)
    Property<LocalDate> orderTime();

    @org.qi4j.library.crudui.Order(30)
    Property<LocalDate> receivedTime();

    @org.qi4j.library.crudui.Order(40)
    Property<List<LocalDate>> fulfilledTimes();

    @org.qi4j.library.crudui.Order(50)
    Property<List<OrderItem>> items();
}
