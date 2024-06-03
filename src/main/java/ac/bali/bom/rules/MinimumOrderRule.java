package ac.bali.bom.rules;

import ac.bali.bom.suppliers.Supply;

public class MinimumOrderRule
    implements Rule
{
    @Override
    public Outcome checkRule(Supply supply, int[] modifiableQuantity )
    {
        if( modifiableQuantity[ 0 ] < supply.minBuyNumber().get() )
        {
            modifiableQuantity[ 0 ] = supply.minBuyNumber().get();
        }
        return Outcome.cont;
    }
}
