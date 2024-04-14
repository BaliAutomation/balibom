package ac.bali.bom.suppliers.lcsc;

import org.apache.polygene.api.common.Optional;
import org.apache.polygene.api.property.Property;

public interface LcscPartResponse
{
    Property<Integer> code();

    @Optional
    Property<String> msg();

    @Optional
    Property<LcscPart> result();
}
