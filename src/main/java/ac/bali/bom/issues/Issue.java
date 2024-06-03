package ac.bali.bom.issues;

import org.apache.polygene.api.identity.HasIdentity;
import org.apache.polygene.api.property.Property;
import org.qi4j.library.crudui.Order;
import org.qi4j.library.crudui.RenderAsName;

public interface Issue extends HasIdentity
{
    @RenderAsName
    @Order(45)
    Property<String> topic();

    @RenderAsName
    @Order(45)
    Property<String> issue();
}
