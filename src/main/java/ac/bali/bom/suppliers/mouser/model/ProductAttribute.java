package ac.bali.bom.suppliers.mouser.model;

import org.apache.polygene.api.common.Optional;
import org.apache.polygene.api.property.Property;

public interface ProductAttribute
{
    Property<String> AttributeName();
    Property<String> AttributeValue();

    @Optional
    Property<String> AttributeCost();
}
