package ac.bali.bom.supply.rules;


import ac.bali.bom.supply.Supply;

public class InStockRule
    implements Rule
{
    @Override
    public Outcome checkRule(SupplyRule supplyRule, Supply supply, int quantity )
    {
        if( supply.inStock().get() + supply.canShipWithInWeek().get() < quantity )
        {
            return new Outcome(Outcome.Progress.abort, quantity);
        }
        return new Outcome(Outcome.Progress.cont, quantity);
    }
}
