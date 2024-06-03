package ac.bali.bom.customers;

import ac.bali.bom.view.Deleter;
import org.apache.polygene.api.identity.HasIdentity;
import org.apache.polygene.api.property.Property;
import org.qi4j.library.crudui.HasListViewController;
import org.qi4j.library.crudui.RenderAsName;

@HasListViewController({CustomerService.class, Deleter.class})
public interface Customer extends HasIdentity
{
    @RenderAsName
    Property<String> name();
}
