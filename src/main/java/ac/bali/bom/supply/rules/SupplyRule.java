package ac.bali.bom.supply.rules;

import ac.bali.bom.supply.Supply;
import java.math.BigDecimal;
import org.apache.polygene.api.common.UseDefaults;
import org.apache.polygene.api.identity.HasIdentity;
import org.apache.polygene.api.injection.scope.This;
import org.apache.polygene.api.mixin.Mixins;
import org.apache.polygene.api.property.Property;
import org.qi4j.library.crudui.Order;
import org.qi4j.library.crudui.RenderAsName;

@Mixins(SupplyRule.Mixin.class)
public interface SupplyRule extends HasIdentity, Comparable<SupplyRule>
{
    Outcome checkRule(Supply supply, int quantity);

    @Order(10)
    @UseDefaults
    Property<Boolean> enabled();

    @Order(20)
    @RenderAsName
    Property<String> name();

    @Order(30)
    Property<RuleType> ruleType();

    @Order(40)
    @UseDefaults
    Property<Integer> ruleOrder();

    @Order(40)
    Property<BigDecimal> value();

    abstract class Mixin implements SupplyRule
    {
        @This
        SupplyRule me;

        @Override
        public Outcome checkRule(Supply supply, int quantity)
        {
            switch (ruleType().get())
            {
                case cheapReel ->
                {
                    return new CheapReelRule().checkRule(me, supply, quantity);
                }
                case inStock ->
                {
                    return new InStockRule().checkRule(me, supply, quantity);
                }
                case marginNeeded ->
                {
                    return new MarginNeededRule().checkRule(me, supply, quantity);
                }
                case minimumOrder ->
                {
                    return new MinimumOrderRule().checkRule(me, supply, quantity);
                }
                case moreThanHalfReel ->
                {
                    return new MoreThanHalfReelRule().checkRule(me, supply, quantity);
                }
                case moreThanOneReel ->
                {
                    return new MoreThanOneReelRule().checkRule(me, supply, quantity);
                }
            }
            System.err.println("Unknown RuleType: " + ruleType().get());
            return new Outcome(Outcome.Progress.abort, 0);
        }

        @Override
        public int compareTo(SupplyRule other)
        {
            return ruleOrder().get().compareTo(other.ruleOrder().get());
        }
    }
}
