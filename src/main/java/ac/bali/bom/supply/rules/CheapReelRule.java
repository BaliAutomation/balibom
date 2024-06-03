package ac.bali.bom.supply.rules;


import ac.bali.bom.supply.Supply;

public class CheapReelRule
    implements Rule
{
    @Override
    public Outcome checkRule(SupplyRule supplyRule, Supply supply, int quantity )
    {
        if( supply.isReel().get())
        {
            int reelSize = supply.reelSize().get();
            if( supply.priceOf(reelSize).doubleValue() < supplyRule.value().get().doubleValue())
            {
                // we are buying a whole reel
                return new Outcome(Outcome.Progress.done, reelSize);
            }
        }
        return new Outcome(Outcome.Progress.cont, quantity);
    }
}
