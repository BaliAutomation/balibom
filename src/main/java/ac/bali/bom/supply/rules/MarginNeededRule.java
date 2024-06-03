package ac.bali.bom.supply.rules;

import ac.bali.bom.supply.Supply;
import java.math.BigDecimal;

public class MarginNeededRule
    implements Rule
{
    @Override
    public Outcome checkRule(SupplyRule supplyRule, Supply supply, int quantity )
    {
        BigDecimal price = supply.priceOf( 10 );
        if( price.doubleValue() < 5.0 )
        {
            quantity += 10;
        }
        else if( price.doubleValue() < 10.0 )
        {
            quantity += 5;
        } else
        {
            quantity += 2;
        }
        return new Outcome(Outcome.Progress.cont, quantity);
    }
}
