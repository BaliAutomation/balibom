package ac.bali.bom.parts;

import ac.bali.bom.suppliers.Supplier;
import ac.bali.bom.ui.support.RenderAsName;
import java.util.SortedSet;
import org.apache.polygene.api.association.Association;
import org.apache.polygene.api.common.UseDefaults;
import org.apache.polygene.api.mixin.Mixins;
import org.apache.polygene.api.property.Immutable;
import org.apache.polygene.api.property.Property;

import java.math.BigDecimal;

@Mixins(Supply.Mixin.class)
public interface Supply {

    BigDecimal priceof(int quantity);

    @RenderAsName
    @Immutable
    Property<String> supplierPartNumber();

    Association<Supplier> supplier();

    Property<String> mf();

    Property<String> mpn();

    Property<Integer> availableSupply();

    Property<Integer> reelSize();

    Property<SortedSet<Price>> prices();

    @UseDefaults
    Property<Boolean> isReel();

    @UseDefaults
    Property<Integer> inStock();

    @UseDefaults
    Property<Integer> canShipWithInWeek();

    @UseDefaults
    Property<Integer> minBuyNumber();

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
