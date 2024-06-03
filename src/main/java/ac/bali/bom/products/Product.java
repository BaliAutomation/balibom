package ac.bali.bom.products;

import ac.bali.bom.view.Deleter;
import java.util.List;
import org.apache.polygene.api.association.Association;
import org.apache.polygene.api.common.Optional;
import org.apache.polygene.api.common.UseDefaults;
import org.apache.polygene.api.entity.Queryable;
import org.apache.polygene.api.identity.HasIdentity;
import org.apache.polygene.api.identity.Identity;
import org.apache.polygene.api.property.Immutable;
import org.apache.polygene.api.property.Property;
import org.qi4j.library.crudui.HasListViewController;
import org.qi4j.library.crudui.Order;
import org.qi4j.library.crudui.RenderAsDescription;
import org.qi4j.library.crudui.RenderAsName;

@HasListViewController({ProductsService.class, ResolveParts.class, Deleter.class})
public interface Product extends HasIdentity
{
    @Order(2)
    @Immutable
    @RenderAsName
    Property<String> name();

    @Order(3)
    @Immutable
    @RenderAsName
    Property<String> revision();

    @UseDefaults("")
    @Order(4)
    @RenderAsDescription
    Property<String> description();

    @Order(5)
    @Queryable(false)
    @Optional
    Property<List<PartQuantity>> parts();

    @Order(6)
    @Queryable(false)
    @UseDefaults
    Property<List<String>> resolveErrors();

    @Order(7)
    @Immutable
    Property<String> bomFile();

    @Order(8)
    @Queryable(false)
    @Immutable
    Association<Bom> bom();
}
