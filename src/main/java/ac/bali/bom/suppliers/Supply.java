package ac.bali.bom.suppliers;

import ac.bali.bom.parts.Price;
import java.util.List;
import java.util.Map;
import org.qi4j.library.javafx.support.Order;
import org.qi4j.library.javafx.support.RenderAsName;
import org.qi4j.library.javafx.support.RenderAsValue;
import java.math.BigDecimal;
import java.util.SortedSet;
import org.apache.polygene.api.association.Association;
import org.apache.polygene.api.common.UseDefaults;
import org.apache.polygene.api.mixin.Mixins;
import org.apache.polygene.api.property.Immutable;
import org.apache.polygene.api.property.Property;

@Mixins(Supply.Mixin.class)
public interface Supply {

    BigDecimal priceof(int quantity);

    @RenderAsName
    @RenderAsValue(title="SPN")
    @Immutable
    @Order(2)
    Property<String> supplierPartNumber();

    @Order(1)
    Association<Supplier> supplier();

    Property<String> mf();

    Property<String> mpn();

    Property<Integer> availableSupply();

    @RenderAsValue(title="Reel Size")
    Property<Integer> reelSize();

    Property<SortedSet<Price>> prices();

    @UseDefaults
    Property<Boolean> isReel();

    @UseDefaults
    Property<Integer> inStock();

    @UseDefaults
    @RenderAsValue(title="Stock")
    Property<Integer> canShipWithInWeek();

    @UseDefaults
    Property<Integer> minBuyNumber();

    @UseDefaults
    Property<String> productIntro();

    Property<List<String>> images();

    Property<String> datasheet();

    Property<Map<String, String>> parameters();

    abstract class Mixin implements Supply {

        @Override
        public BigDecimal priceof(int quantity) {
            BigDecimal myPrice = new BigDecimal( 0 );
            for( Price price : prices().get() )
            {
                if( price.quantity().get() <= quantity )
                {
                    myPrice = price.price().get();
                }
            }
            return myPrice.multiply( new BigDecimal( quantity ) );
        }
    }
}
