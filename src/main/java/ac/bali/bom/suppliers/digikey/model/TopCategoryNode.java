package ac.bali.bom.suppliers.digikey.model;

import org.apache.polygene.api.property.Property;

@SuppressWarnings("unused")
public interface TopCategoryNode
{
    Property<Integer> Id();
    Property<String> Name();
    Property<Long> ProductCount();
}
