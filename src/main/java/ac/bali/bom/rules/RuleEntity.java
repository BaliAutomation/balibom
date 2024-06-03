package ac.bali.bom.rules;

import ac.bali.bom.suppliers.Supply;
import org.apache.polygene.api.common.UseDefaults;
import org.apache.polygene.api.identity.HasIdentity;
import org.apache.polygene.api.mixin.Mixins;
import org.apache.polygene.api.property.Property;
import org.qi4j.library.crudui.Order;
import org.qi4j.library.crudui.RenderAsName;

@Mixins(RuleEntity.Mixin.class)
public interface RuleEntity extends Rule, HasIdentity
{
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

    abstract class Mixin implements RuleEntity
    {
        @Override
        public Outcome checkRule(Supply supply, int[] modifiableQuantity)
        {
            switch (ruleType().get())
            {
                case cheapReel ->
                {
                    return new CheapReelRule().checkRule(supply, modifiableQuantity);
                }
                case inStock ->
                {
                    return new InStockRule().checkRule(supply, modifiableQuantity);
                }
                case marginNeeded ->
                {
                    return new MarginNeededRule().checkRule(supply, modifiableQuantity);
                }
                case minimumOrder ->
                {
                    return new MinimumOrderRule().checkRule(supply, modifiableQuantity);
                }
                case moreThanHalfReel ->
                {
                    return new MoreThanHalfReelRule().checkRule(supply, modifiableQuantity);
                }
                case moreThanOneReel ->
                {
                    return new MoreThanOneReelRule().checkRule(supply, modifiableQuantity);
                }
            }
            System.err.println("Unknown RuleType: " + ruleType().get());
            return Outcome.abort;
        }
    }
}
