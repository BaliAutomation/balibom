package ac.bali.bom.inventory;

import ac.bali.bom.parts.Part;
import org.qi4j.library.javafx.support.RenderAsName;
import org.apache.polygene.api.association.Association;
import org.apache.polygene.api.identity.HasIdentity;
import org.apache.polygene.api.property.Immutable;
import org.apache.polygene.api.property.Property;

public interface PartsInventory extends HasIdentity
{
    @RenderAsName
    @Immutable
    Association<Part> part();

    Property<Integer> quantity();
}
