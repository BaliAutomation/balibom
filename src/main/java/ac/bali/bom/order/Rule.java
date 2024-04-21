package ac.bali.bom.order;

import ac.bali.bom.suppliers.Supply;

public interface Rule
{
    Outcome checkRule(Supply supply, int[] modifiableQuantity);
}
