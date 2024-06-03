package ac.bali.bom.supply.digikey.model;

import org.apache.polygene.api.common.Optional;
import org.apache.polygene.api.property.Property;

@SuppressWarnings("unused")
public interface BaseFilterV4
{
    Property<Integer> Id();

    Property<String> Value();

    @Optional
    Property<Long> ProductCount();
}
