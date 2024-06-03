package ac.bali.bom.inventory.products;

import ac.bali.bom.products.Product;
import ac.bali.bom.view.Deleter;
import org.apache.polygene.api.association.Association;
import org.apache.polygene.api.common.UseDefaults;
import org.apache.polygene.api.identity.HasIdentity;
import org.apache.polygene.api.property.Immutable;
import org.apache.polygene.api.property.Property;
import org.qi4j.library.crudui.HasListViewController;
import org.qi4j.library.crudui.RenderAsName;

@HasListViewController({ProductsInventoryService.class, Deleter.class})
public interface ProductsInventory extends HasIdentity
{
    @RenderAsName
    @Immutable
    Association<Product> product();

    @UseDefaults
    Property<Integer> quantity();
}
