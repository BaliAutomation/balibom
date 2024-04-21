package ac.bali.bom.suppliers.digikey.model;

import org.apache.polygene.api.property.Property;

@SuppressWarnings("unused")
public interface Supplier
{
    Property<Integer> Id();
    Property<String> Name();
}
