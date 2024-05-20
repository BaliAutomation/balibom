package ac.bali.bom.suppliers.digikey.model;

import java.math.BigDecimal;
import org.apache.polygene.api.property.Property;

@SuppressWarnings("unused")
public interface PriceBreak
{
    Property<Integer> BreakQuantity();

    Property<BigDecimal> UnitPrice();

    Property<BigDecimal> TotalPrice();
}
