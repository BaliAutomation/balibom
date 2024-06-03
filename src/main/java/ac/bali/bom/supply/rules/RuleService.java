package ac.bali.bom.supply.rules;

import ac.bali.bom.supply.Supply;
import ac.bali.bom.view.Deleter;
import org.apache.polygene.api.entity.EntityBuilder;
import org.apache.polygene.api.identity.Identity;
import org.apache.polygene.api.identity.StringIdentity;
import org.apache.polygene.api.injection.scope.Structure;
import org.apache.polygene.api.mixin.Mixins;
import org.apache.polygene.api.query.Query;
import org.apache.polygene.api.query.QueryBuilder;
import org.apache.polygene.api.query.QueryBuilderFactory;
import org.apache.polygene.api.query.grammar.OrderBy;
import org.apache.polygene.api.unitofwork.UnitOfWork;
import org.apache.polygene.api.unitofwork.UnitOfWorkFactory;
import org.apache.polygene.api.unitofwork.concern.UnitOfWorkPropagation;
import org.qi4j.library.crudui.Action;
import org.qi4j.library.crudui.HasListViewController;

import static org.apache.polygene.api.query.QueryExpressions.eq;
import static org.apache.polygene.api.query.QueryExpressions.templateFor;
import static org.apache.polygene.api.unitofwork.concern.UnitOfWorkPropagation.Propagation.MANDATORY;
import static org.qi4j.library.crudui.ActionScope.type;

@HasListViewController({RuleService.class, Deleter.class})
@Mixins(RuleService.Mixin.class)
public interface RuleService
{
    @UnitOfWorkPropagation(MANDATORY)
    @Action(label = "New...", scope = type)
    void createRule(String name, RuleType ruleType);

    @UnitOfWorkPropagation(MANDATORY)
    int applyQuantityRules(Supply supply, int quantity);

    class Mixin
        implements RuleService
    {
        @Structure
        private UnitOfWorkFactory uowf;

        @Structure
        private QueryBuilderFactory qbf;

        @Override
        public void createRule(String name, RuleType ruleType)
        {
            UnitOfWork uow = uowf.currentUnitOfWork();
            Identity identity = StringIdentity.identityOf("rule/" + name);
            EntityBuilder<SupplyRule> builder = uow.newEntityBuilder(SupplyRule.class, identity);
            SupplyRule instance = builder.instance();
            instance.name().set(name);
            instance.ruleType().set(ruleType);
            instance.enabled().set(true);
            builder.newInstance();
        }

        @Override
        public int applyQuantityRules(Supply supply, int quantity)
        {
            UnitOfWork uow = uowf.currentUnitOfWork();
            QueryBuilder<SupplyRule> qb = qbf.newQueryBuilder(SupplyRule.class);
            SupplyRule supplyRule = templateFor(SupplyRule.class);
            qb = qb.where(eq(supplyRule.enabled(), true));
            Query<SupplyRule> query = uow.newQuery(qb);
            query.orderBy(supplyRule.ruleOrder(), OrderBy.Order.ASCENDING);
            for( SupplyRule rule : query)
            {
                Outcome outcome = rule.checkRule(supply, quantity);
                quantity = outcome.quantity();
                if (outcome.progress() == Outcome.Progress.abort)
                {
                    System.err.println("Rule [" + rule + "] aborted");
                    return 0;
                }
                if (outcome.progress() == Outcome.Progress.done)
                    return quantity;
            }
            return 0;
        }
    }
}
