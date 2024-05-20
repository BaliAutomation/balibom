package ac.bali.bom.order.rules;

import ac.bali.bom.order.Outcome;
import ac.bali.bom.order.Rule;
import ac.bali.bom.suppliers.Supply;
import java.math.BigDecimal;

public class MarginNeededRule
    implements Rule
{
    @Override
    public Outcome checkRule(Supply supply, int[] modifiableQuantity )
    {
        BigDecimal price = supply.priceOf( 10 );
        if( price.doubleValue() < 5.0 )
        {
            modifiableQuantity[ 0 ] += 10;
        }
        else if( price.doubleValue() < 10.0 )
        {
            modifiableQuantity[ 0 ] += 5;
        } else
        {
            modifiableQuantity[ 0 ] += 2;
        }
        return Outcome.cont;
    }
}
