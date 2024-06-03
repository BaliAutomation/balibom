package ac.bali.bom.rules;

import ac.bali.bom.suppliers.Supply;
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
    int applyQuantityRules(Supply supply);

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
            EntityBuilder<RuleEntity> builder = uow.newEntityBuilder(RuleEntity.class, identity);
            RuleEntity instance = builder.instance();
            instance.name().set(name);
            instance.ruleType().set(ruleType);
            instance.enabled().set(true);
            builder.newInstance();
        }

        @Override
        public int applyQuantityRules(Supply supply)
        {
            UnitOfWork uow = uowf.currentUnitOfWork();
            QueryBuilder<RuleEntity> qb = qbf.newQueryBuilder(RuleEntity.class);
            RuleEntity ruleEntity = templateFor(RuleEntity.class);
            qb = qb.where(eq(ruleEntity.enabled(), true));
            Query<RuleEntity> query = uow.newQuery(qb);
            query.orderBy(ruleEntity.ruleOrder(), OrderBy.Order.ASCENDING);

            int[] modifiableQuantity = new int[1];
            for (Rule rule : query)
            {
                Outcome outcome = rule.checkRule(supply, modifiableQuantity);
                if (outcome == Outcome.abort)
                {
                    System.err.println("Rule [" + rule + "] aborted");
                    return 0;
                }
                if (outcome == Outcome.done)
                    return modifiableQuantity[0];
            }
            return 0;
        }
    }
}
