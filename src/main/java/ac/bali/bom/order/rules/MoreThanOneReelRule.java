package ac.bali.bom.order.rules;

import ac.bali.bom.order.Outcome;
import ac.bali.bom.order.Rule;
import ac.bali.bom.suppliers.Supply;

public class MoreThanOneReelRule
    implements Rule
{
    @Override
    public Outcome checkRule(Supply supply, int[] modifiableQuantity )
    {
        int quantity = modifiableQuantity[ 0 ];
        if( supply.isReel().get() )
        {
            int reelSize = supply.reelSize().get();
            if( quantity > reelSize )
            {
                // we buy full reels only
                int numberOfReels = quantity / reelSize;
                modifiableQuantity[ 0 ] = numberOfReels * reelSize;
                return Outcome.done;
            }
        }
        return Outcome.cont;
    }
}
