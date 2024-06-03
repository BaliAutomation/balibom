package ac.bali.bom.manufacturers;

import ac.bali.bom.view.Deleter;
import java.util.Set;
import org.apache.polygene.api.mixin.Mixins;
import org.qi4j.library.crudui.HasListViewController;
import org.qi4j.library.crudui.Order;
import org.qi4j.library.crudui.RenderAsName;
import org.apache.polygene.api.common.UseDefaults;
import org.apache.polygene.api.identity.HasIdentity;
import org.apache.polygene.api.property.Property;

@HasListViewController({ManufacturersService.class, Deleter.class})
@Mixins(ManufacturerHashCodeEquals.class)
public interface Manufacturer extends HasIdentity {

    @UseDefaults
    @Order(2)
    @RenderAsName
    Property<String> identifier();

    @UseDefaults
    @Order(3)
    Property<String> fullName();

    @UseDefaults
    @Order(4)
    Property<String> country();

    @UseDefaults
    @Order(5)
    Property<Set<String>> alternateNames();

    boolean equals(Object o);

    int hashCode();
}
