package ac.bali.bom.order.rules;

import ac.bali.bom.order.Outcome;
import ac.bali.bom.order.Rule;
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
