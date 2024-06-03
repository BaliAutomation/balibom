package ac.bali.bom.supply.rules;

import ac.bali.bom.supply.Supply;
import ac.bali.bom.view.Deleter;
import org.qi4j.library.crudui.HasListViewController;

@HasListViewController({RuleService.class, Deleter.class})
public interface Rule
{
    Outcome checkRule(SupplyRule supplyRule, Supply supply, int proposedQuantity);
}
