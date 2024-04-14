package ac.bali.bom.parts;

import java.math.BigDecimal;
import java.util.Comparator;
import org.apache.polygene.api.property.Property;

public interface Price
{
    Property<Integer> quantity();
    Property<BigDecimal> price();

    class PriceComparator
        implements Comparator<Price>
    {
        @Override
        public int compare(Price o1, Price o2)
        {
            return o1.quantity().get().compareTo(o2.quantity().get());
        }
    }
}
