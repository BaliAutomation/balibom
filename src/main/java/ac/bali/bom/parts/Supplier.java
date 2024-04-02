package ac.bali.bom.parts;

import ac.bali.bom.support.RenderAsName;
import org.apache.polygene.api.identity.HasIdentity;
import org.apache.polygene.api.property.Property;

public interface Supplier extends HasIdentity {

    @RenderAsName
    Property<String> name();

    Property<String> website();

    Property<String> search();

    Property<String> api();

}
