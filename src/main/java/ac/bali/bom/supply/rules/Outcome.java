package ac.bali.bom.supply.rules;

public record Outcome( Progress progress, int quantity)
{
    enum Progress
    {
        /**
         * Stop trying to order this Part at this Supplier
         */
        abort,
        /**
         * Continue checking the rules for this Part at this Supplier
         */
        cont,
        /**
         * Quantity of this Part has been established for this Supplier
         */
        done
    }
}
