package ac.bali.bom.products;

import ac.bali.bom.ui.support.HasListViewController;
import ac.bali.bom.ui.support.Order;
import ac.bali.bom.ui.support.RenderAsDescription;
import ac.bali.bom.ui.support.RenderAsName;
import ac.bali.bom.view.ResolveParts;
import org.apache.polygene.api.common.UseDefaults;
import org.apache.polygene.api.entity.Queryable;
import org.apache.polygene.api.identity.HasIdentity;
import org.apache.polygene.api.property.Immutable;
import org.apache.polygene.api.property.Property;

@HasListViewController({ProductsService.class, ResolveParts.class})
public interface Product extends HasIdentity
{
    @Order(1)
    @Immutable
    @RenderAsName
    Property<String> name();

    @Order(2)
    @Immutable
    @RenderAsName
    Property<String> revision();

    @UseDefaults("")
    @Order(5)
    @RenderAsDescription
    Property<String> description();

    @Order(3)
    @Immutable
    Property<String> bomFile();

    @Order(4)
    @Queryable(false)
    @Immutable
    Property<Bom> bom();

}
