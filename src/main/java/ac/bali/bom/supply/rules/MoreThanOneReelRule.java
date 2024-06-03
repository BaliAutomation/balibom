package ac.bali.bom.supply.rules;

import ac.bali.bom.supply.Supply;

public class MoreThanOneReelRule
    implements Rule
{
    @Override
    public Outcome checkRule(SupplyRule supplyRule, Supply supply, int quantity )
    {
        if( supply.isReel().get() )
        {
            int reelSize = supply.reelSize().get();
            if( quantity > reelSize )
            {
                // we buy full reels only
                int numberOfReels = quantity / reelSize;
                quantity = numberOfReels * reelSize;
                return new Outcome(Outcome.Progress.done, quantity);
            }
        }
        return new Outcome(Outcome.Progress.cont, quantity);
    }
}
