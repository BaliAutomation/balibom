package ac.bali.bom.suppliers.mouser.model;

import org.apache.polygene.api.property.Property;

public interface ProductAttribute
{
    Property<String> AttributeName();
    Property<String> AttributeValue();
    Property<String> AttributeCost();
}
