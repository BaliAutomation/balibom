package ac.bali.bom.lcsc;

import org.apache.polygene.api.property.Property;

import java.math.BigDecimal;

public interface LcscPartPrice {
    Property<Integer> ladder();
    Property<BigDecimal> productPrice();
    Property<BigDecimal> discountRate();
    Property<BigDecimal> currencyPrice();
    Property<BigDecimal> usdPrice();
    Property<String> currencySymbol();
    Property<BigDecimal> cnyProductPrice();

}
