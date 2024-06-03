package ac.bali.bom.supply.lcsc;

import org.apache.polygene.api.common.Optional;
import org.apache.polygene.api.property.Property;

public interface LcscSearchResponse
{
    Property<Integer> code();

    @Optional
    Property<String> msg();

    Property<LcscSearch> result();

}
