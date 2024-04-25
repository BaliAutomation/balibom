package ac.bali.bom.jobs;

import ac.bali.bom.parts.PartsService;
import ac.bali.bom.products.Product;
import org.apache.polygene.api.association.Association;
import org.apache.polygene.api.identity.HasIdentity;
import org.apache.polygene.api.property.Property;
import org.qi4j.library.javafx.support.HasListViewController;

@HasListViewController(JobsService.class)
public interface Job extends HasIdentity
{
    Property<Integer> quantity();
    Association<Product> product();
}