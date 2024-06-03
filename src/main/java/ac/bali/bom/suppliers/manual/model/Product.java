package ac.bali.bom.suppliers.manual.model;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.apache.polygene.api.property.Property;

public interface Product
{
    Property<List<String>> sources();
    Property<String> manufacturer();
    Property<String> manufacturerPartNumber();
    Property<String> supplierPartNumber();
    Property<String> partDescription();
    Property<String> datasheetUrl();
    Property<Integer> quantityAvailable();
    Property<Boolean> isReel();
    Property<Integer> reelSize();
    Property<Integer> minimumPurchase();
    Property<Map<Integer, BigDecimal>> prices();
}
