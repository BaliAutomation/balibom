package ac.bali.bom.order;

import ac.bali.bom.parts.Supply;

public interface Rule
{
    Outcome checkRule(Supply supply, int[] modifiableQuantity);
}
