package ac.bali.bom.jobs;

import ac.bali.bom.customers.Customer;
import ac.bali.bom.products.Product;
import ac.bali.bom.view.AssociationFormatter;
import ac.bali.bom.view.Deleter;
import java.time.LocalDate;
import org.apache.polygene.api.association.Association;
import org.apache.polygene.api.common.UseDefaults;
import org.apache.polygene.api.identity.HasIdentity;
import org.apache.polygene.api.property.Property;
import org.qi4j.library.crudui.HasListViewController;
import org.qi4j.library.crudui.Order;
import org.qi4j.library.crudui.RenderAsName;

@HasListViewController({JobsService.class, Deleter.class})
public interface Job extends HasIdentity
{
    @RenderAsName(format = AssociationFormatter.class )
    @Order(10)
    Association<Customer> customer();

    @Order(20)
    Property<Integer> quantity();

    @RenderAsName
    @Order(30)
    Property<LocalDate> deadline();

    @RenderAsName
    @Order(45)
    Property<LocalDate> completion();

    @RenderAsName(format = AssociationFormatter.class )
    @Order(40)
    Association<Product> product();

    @Order(50)
    @UseDefaults("initial")
    Property<JobState> state();
}
