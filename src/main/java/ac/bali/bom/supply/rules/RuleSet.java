package ac.bali.bom.supply.rules;

import ac.bali.bom.supply.Supplier;
import ac.bali.bom.view.Deleter;
import org.apache.polygene.api.association.Association;
import org.apache.polygene.api.association.ManyAssociation;
import org.apache.polygene.api.identity.HasIdentity;
import org.apache.polygene.api.property.Property;
import org.qi4j.library.crudui.HasListViewController;
import org.qi4j.library.crudui.Order;
import org.qi4j.library.crudui.RenderAsName;

@HasListViewController({RuleSetService.class, Deleter.class})
public interface RuleSet extends HasIdentity
{
    @RenderAsName
    @Order(10)
    Property<String> name();

    Association<Supplier> supplier();

    ManyAssociation<SupplyRule> rules();
}
