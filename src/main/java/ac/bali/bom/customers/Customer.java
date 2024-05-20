package ac.bali.bom.customers;

import ac.bali.bom.jobs.JobsService;
import org.apache.polygene.api.identity.HasIdentity;
import org.apache.polygene.api.property.Property;
import org.qi4j.library.javafx.support.HasListViewController;
import org.qi4j.library.javafx.support.RenderAsName;

@HasListViewController(CustomerService.class)
public interface Customer extends HasIdentity
{
    @RenderAsName
    Property<String> name();
}
