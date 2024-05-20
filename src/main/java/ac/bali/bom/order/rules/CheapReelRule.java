package ac.bali.bom.order.rules;


import ac.bali.bom.order.Outcome;
import ac.bali.bom.order.Rule;
import ac.bali.bom.suppliers.Supply;

public class CheapReelRule
    implements Rule
{
    @Override
    public Outcome checkRule(Supply supply, int[] modifiableQuantity )
    {
        if( supply.isReel().get())
        {
            int reelSize = supply.reelSize().get();
            if( supply.priceOf(reelSize).doubleValue() < 50.0)
            {
                // we are buying a whole reel
                modifiableQuantity[0] = reelSize;
                return Outcome.done;
            }
        }
        return Outcome.cont;
    }
}
