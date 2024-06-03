package ac.bali.bom.rules;


import ac.bali.bom.suppliers.Supply;

public class InStockRule
    implements Rule
{
    @Override
    public Outcome checkRule(Supply supply, int[] modifiableQuantity )
    {
        int quantity = modifiableQuantity[0];
        if( supply.inStock().get() + supply.canShipWithInWeek().get() < quantity )
        {
            return Outcome.abort;
        }
        return Outcome.cont;
    }
}
