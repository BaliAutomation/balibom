package ac.bali.bom.supply.rules;

import ac.bali.bom.supply.Supply;
import java.math.BigDecimal;

public class MoreThanHalfReelRule
    implements Rule
{
    @Override
    public Outcome checkRule(SupplyRule supplyRule, Supply supply, int quantity )
    {
        if( supply.isReel().get() )
        {
            int reelSize = supply.reelSize().get();
            BigDecimal exactPrice = supply.priceOf( quantity );
            BigDecimal reelPrice = supply.priceOf( reelSize );
            if( (reelPrice.doubleValue() / 2) < exactPrice.doubleValue() )
            {
                quantity = reelSize;
                return new Outcome(Outcome.Progress.done, quantity);
            }
        }
        return new Outcome(Outcome.Progress.cont, quantity);
    }
}
