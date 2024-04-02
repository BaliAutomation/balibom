package ac.bali.bom.parts;

import ac.bali.bom.support.RenderAsName;
import org.apache.polygene.api.association.Association;
import org.apache.polygene.api.identity.HasIdentity;
import org.apache.polygene.api.mixin.Mixins;
import org.apache.polygene.api.property.Immutable;
import org.apache.polygene.api.property.Property;

import java.math.BigDecimal;
import java.util.Map;

@Mixins(Supply.Mixin.class)
public interface Supply extends HasIdentity {

    BigDecimal priceof(int quantity);

    @RenderAsName
    @Immutable
    Property<String> partNumber();

    @RenderAsName
    Association<Supplier> supplier();

    Property<Long> availableSupply();

    Property<Integer> reelSize();

    Property<Map<Integer, BigDecimal>> prices();

    abstract class Mixin implements Supply {

        @Override
        public BigDecimal priceof(int quantity) {
            BigDecimal myPrice = new BigDecimal( 0 );
            for( Map.Entry<Integer, BigDecimal> price : prices().get().entrySet() )
            {
                if( price.getKey() <= quantity )
                {
                    myPrice = price.getValue();
                }
            }
            return myPrice.multiply( new BigDecimal( quantity ) );
        }
    }
}
