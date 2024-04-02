package ac.bali.bom.connectivity;

import ac.bali.bom.parts.Part;
import ac.bali.bom.parts.Supply;

import java.util.List;

public interface PartSupplyConnector {

    Part findPart(String supplierPartNumber);

    List<Supply> search(String freetext);
}
