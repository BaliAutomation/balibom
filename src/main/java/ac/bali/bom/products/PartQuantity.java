package ac.bali.bom.products;

import ac.bali.bom.parts.Part;
import org.apache.polygene.api.association.Association;
import org.apache.polygene.api.property.Property;

public interface PartQuantity
{
    Association<Part> part();

    Property<Integer> quantity();
}
