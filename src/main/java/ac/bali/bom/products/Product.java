package ac.bali.bom.products;

import ac.bali.bom.view.Delete;
import org.qi4j.library.javafx.support.HasListViewController;
import org.qi4j.library.javafx.support.Order;
import org.qi4j.library.javafx.support.RenderAsDescription;
import org.qi4j.library.javafx.support.RenderAsName;
import ac.bali.bom.view.ResolveParts;
import org.apache.polygene.api.common.UseDefaults;
import org.apache.polygene.api.entity.Queryable;
import org.apache.polygene.api.identity.HasIdentity;
import org.apache.polygene.api.property.Immutable;
import org.apache.polygene.api.property.Property;

@HasListViewController({ProductsService.class, ResolveParts.class, Delete.class})
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
