package ac.bali.bom.supply.digikey.model;

import org.apache.polygene.api.common.Optional;
import org.apache.polygene.api.property.Property;

@SuppressWarnings("unused")
public interface FilterValue
{
    Property<Long> ProductCount();

    Property<String> ValueId();

    Property<String> ValueName();

    @Optional
    Property<RangeFilterTypeEnum> RangeFilterType();
}
