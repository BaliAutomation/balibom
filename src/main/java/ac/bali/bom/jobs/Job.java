package ac.bali.bom.jobs;

import ac.bali.bom.customers.Customer;
import ac.bali.bom.products.Product;
import ac.bali.bom.view.AssociationFormatter;
import java.time.LocalDate;
import org.apache.polygene.api.association.Association;
import org.apache.polygene.api.identity.HasIdentity;
import org.apache.polygene.api.property.Property;
import org.qi4j.library.javafx.support.HasListViewController;
import org.qi4j.library.javafx.support.Order;
import org.qi4j.library.javafx.support.RenderAsName;

@HasListViewController(JobsService.class)
public interface Job extends HasIdentity
{
    @RenderAsName(format = AssociationFormatter.class )
    @Order(1)
    Association<Customer> customer();

    @Order(2)
    Property<Integer> quantity();

    @RenderAsName
    @Order(3)
    Property<LocalDate> deadline();

    @RenderAsName(format = AssociationFormatter.class )
    @Order(4)
    Association<Product> product();
}
