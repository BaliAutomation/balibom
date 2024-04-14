package ac.bali.bom.products;

import ac.bali.bom.ui.support.RenderAsName;
import java.util.Map;
import org.apache.polygene.api.common.Optional;
import org.apache.polygene.api.common.UseDefaults;
import org.apache.polygene.api.property.Property;

public interface BomItem
{
    @Optional
    Property<String> mf();

    @UseDefaults
    Property<String> mpn();

    @UseDefaults
    Property<String> value();

    @UseDefaults
    Property<String> footprint();

    @RenderAsName
    Property<String> designator();

    @UseDefaults
    Property<Integer> quantity();

    @UseDefaults
    Property<Map<String,String>> attributes();
}
