package ac.bali.bom.rules;

import ac.bali.bom.suppliers.Supply;
import ac.bali.bom.view.Deleter;
import org.qi4j.library.crudui.HasListViewController;

@HasListViewController({RuleService.class, Deleter.class})
public interface Rule
{
    Outcome checkRule(Supply supply, int[] modifiableQuantity);
}
