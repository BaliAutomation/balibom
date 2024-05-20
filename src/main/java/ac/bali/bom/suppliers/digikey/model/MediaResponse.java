package ac.bali.bom.suppliers.digikey.model;

import java.util.List;
import org.apache.polygene.api.common.UseDefaults;
import org.apache.polygene.api.property.Property;

@SuppressWarnings("unused")
public interface MediaResponse
{
    @UseDefaults
    Property<List<MediaLinks>> MediaLinks();
}
