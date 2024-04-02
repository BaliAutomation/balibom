package ac.bali.bom.parts;

import ac.bali.bom.support.RenderAsName;
import org.apache.polygene.api.association.Association;
import org.apache.polygene.api.association.ManyAssociation;
import org.apache.polygene.api.association.NamedAssociation;
import org.apache.polygene.api.common.UseDefaults;
import org.apache.polygene.api.identity.HasIdentity;
import org.apache.polygene.api.property.Property;

import java.util.Map;

@SuppressWarnings("unused")
public interface Part extends HasIdentity {

    @RenderAsName
    Association<Manufacturer> manufacturer();

    @RenderAsName
    Property<String> mpn();

    @UseDefaults("-1")
    Property<Long> fullReelSize();

    @UseDefaults
    NamedAssociation<Supply> supply();

    @UseDefaults
    ManyAssociation<Part> alternates();

    @UseDefaults
    Property<Map<String, String>> parameters();
}
