package ac.bali.bom.suppliers.digikey.model;

import org.apache.polygene.api.property.Property;

@SuppressWarnings("unused")
public interface Parameter
{
    Property<Integer> Id();
    Property<String> Text();
    Property<Integer> Priority();
}
