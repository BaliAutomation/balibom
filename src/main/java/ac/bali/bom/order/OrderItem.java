package ac.bali.bom.order;

import ac.bali.bom.parts.Part;
import org.apache.polygene.api.association.Association;
import org.apache.polygene.api.property.Property;

public interface OrderItem {

    Association<Part> part();
    Property<Integer> quantity();
}
