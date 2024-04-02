package ac.bali.bom.products;

import ac.bali.bom.parts.Manufacturer;
import ac.bali.bom.support.RenderAsName;
import org.apache.polygene.api.association.Association;
import org.apache.polygene.api.common.Optional;
import org.apache.polygene.api.common.UseDefaults;
import org.apache.polygene.api.property.Property;

public interface BomItem
{
    @Optional
    Association<Manufacturer> manufacturer();

    @UseDefaults
    Property<String> mpn();

    @UseDefaults
    Property<String> value();

    @UseDefaults
    Property<String> footprint();

    @RenderAsName
    Property<String> designator();

    Property<Integer> quantity();
}
