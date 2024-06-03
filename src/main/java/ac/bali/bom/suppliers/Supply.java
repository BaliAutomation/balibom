package ac.bali.bom.suppliers;

import ac.bali.bom.manufacturers.Manufacturer;
import ac.bali.bom.parts.Price;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.apache.polygene.api.association.Association;
import org.apache.polygene.api.common.UseDefaults;
import org.apache.polygene.api.mixin.Mixins;
import org.apache.polygene.api.property.Immutable;
import org.apache.polygene.api.property.Property;
import org.qi4j.library.crudui.Order;
import org.qi4j.library.crudui.RenderAsName;
import org.qi4j.library.crudui.RenderAsValue;

@Mixins(Supply.Mixin.class)
public interface Supply
{

    BigDecimal priceOf(int quantity);

    Property<LocalDate> updatedOn();

    @RenderAsName
    @RenderAsValue(title = "SPN")
    @Immutable
    @Order(2)
    Property<String> supplierPartNumber();

    @Order(1)
    Association<Supplier> supplier();

    Association<Manufacturer> mf();

    Property<String> mpn();

    Property<Integer> availableSupply();

    @RenderAsValue(title = "Reel Size")
    Property<Integer> reelSize();

    Property<Set<Price>> prices();

    @UseDefaults
    Property<Boolean> isReel();

    @UseDefaults
    Property<Integer> inStock();

    @UseDefaults
    @RenderAsValue(title = "Stock")
    Property<Integer> canShipWithInWeek();

    @UseDefaults
    Property<Integer> minBuyNumber();

    @UseDefaults
    Property<String> productIntro();

    Property<List<String>> images();

    Property<String> datasheet();

    Property<Map<String, String>> parameters();

    abstract class Mixin implements Supply
    {
        @Override
        public BigDecimal priceOf(int quantity)
        {
            Price unitPrice = prices().get()
                .stream()
                .filter(Objects::nonNull)
                .sorted(new Price.PriceComparator().reversed())
                .filter(p -> quantity >= p.quantity().get())
                .findFirst().orElse(null);
            if (unitPrice == null)
                return null;
            return unitPrice.price().get().multiply(new BigDecimal(quantity));
        }
    }
}
