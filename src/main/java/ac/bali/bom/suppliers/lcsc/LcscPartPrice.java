package ac.bali.bom.suppliers.lcsc;

import java.math.BigDecimal;
import org.apache.polygene.api.common.Optional;
import org.apache.polygene.api.property.Property;

@SuppressWarnings("unused")
public interface LcscPartPrice {
    Property<Integer> ladder();
    @Optional
    Property<BigDecimal> productPrice();
    @Optional
    Property<BigDecimal> discountRate();
    @Optional
    Property<BigDecimal> currencyPrice();
    Property<BigDecimal> usdPrice();
    @Optional
    Property<String> currencySymbol();
    @Optional
    Property<BigDecimal> cnyProductPrice();

}
