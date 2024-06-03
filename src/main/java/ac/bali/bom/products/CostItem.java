package ac.bali.bom.products;

import ac.bali.bom.parts.Part;
import ac.bali.bom.supply.Supplier;
import java.math.BigDecimal;
import org.apache.polygene.api.association.Association;
import org.apache.polygene.api.property.Property;

public interface CostItem
{
    Association<Part> part();
    Association<Supplier> supplier();
    Property<String> supplierPartNumber();
    Property<BigDecimal> price();
    Property<Integer> quantity();
}
