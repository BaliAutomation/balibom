package ac.bali.bom.lcsc;

import org.apache.polygene.api.property.Property;

public interface LcscResponse {

        Property<Integer> code();
        Property<String> msg();
        Property<LcscPart> result();
}
