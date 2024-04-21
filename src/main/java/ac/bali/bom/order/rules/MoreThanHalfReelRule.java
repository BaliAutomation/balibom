package ac.bali.bom.order.rules;

import ac.bali.bom.order.Outcome;
import ac.bali.bom.order.Rule;
import ac.bali.bom.suppliers.Supply;
import java.math.BigDecimal;

public class MoreThanHalfReelRule
    implements Rule
{
    @Override
    public Outcome checkRule(Supply supply, int[] modifiableQuantity )
    {
        int quantity = modifiableQuantity[ 0 ];
        if( supply.isReel().get() )
        {
            int reelSize = supply.reelSize().get();
            BigDecimal exactPrice = supply.priceof( quantity );
            BigDecimal reelPrice = supply.priceof( reelSize );
            if( (reelPrice.doubleValue() / 2) < exactPrice.doubleValue() )
            {
                modifiableQuantity[0] = reelSize;
                return Outcome.done;
            }
        }
        return Outcome.cont;
    }
}
