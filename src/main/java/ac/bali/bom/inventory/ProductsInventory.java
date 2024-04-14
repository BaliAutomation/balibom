package ac.bali.bom.inventory;

import ac.bali.bom.products.Product;
import ac.bali.bom.ui.support.RenderAsName;
import org.apache.polygene.api.association.Association;
import org.apache.polygene.api.identity.HasIdentity;
import org.apache.polygene.api.property.Immutable;
import org.apache.polygene.api.property.Property;

public interface ProductsInventory extends HasIdentity
{
    @RenderAsName
    @Immutable
    Association<Product> product();

    Property<Integer> quantity();
}
