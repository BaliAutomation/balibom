package ac.bali.bom.inventory.parts;

import ac.bali.bom.parts.Part;
import ac.bali.bom.view.Deleter;
import org.apache.polygene.api.common.UseDefaults;
import org.qi4j.library.crudui.HasListViewController;
import org.qi4j.library.crudui.RenderAsName;
import org.apache.polygene.api.association.Association;
import org.apache.polygene.api.identity.HasIdentity;
import org.apache.polygene.api.property.Immutable;
import org.apache.polygene.api.property.Property;

@HasListViewController({PartsInventoryService.class, Deleter.class})
public interface PartsInventory extends HasIdentity
{
    @RenderAsName
    @Immutable
    Association<Part> part();

    @UseDefaults
    Property<Integer> quantity();

    @UseDefaults("unknown")
    Property<String> location();
}
