package ac.bali.bom.supply.rules;

import ac.bali.bom.supply.Supply;

public class MinimumOrderRule
    implements Rule
{
    @Override
    public Outcome checkRule(SupplyRule supplyRule, Supply supply, int quantity )
    {
        if( quantity < supply.minBuyNumber().get() )
        {
            quantity = supply.minBuyNumber().get();
        }
        return new Outcome(Outcome.Progress.cont, quantity);
    }
}
