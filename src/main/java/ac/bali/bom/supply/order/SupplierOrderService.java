package ac.bali.bom.supply.order;

import ac.bali.bom.supply.rules.RuleService;
import ac.bali.bom.parts.Part;
import ac.bali.bom.supply.rules.RuleSet;
import java.util.Map;
import org.apache.polygene.api.injection.scope.Service;
import org.apache.polygene.api.injection.scope.Structure;
import org.apache.polygene.api.mixin.Mixins;
import org.apache.polygene.api.unitofwork.UnitOfWork;
import org.apache.polygene.api.unitofwork.UnitOfWorkFactory;
import org.apache.polygene.api.unitofwork.concern.UnitOfWorkPropagation;

import static org.apache.polygene.api.unitofwork.concern.UnitOfWorkPropagation.Propagation.MANDATORY;

@Mixins(SupplierOrderService.Mixin.class)
public interface SupplierOrderService
{
    @UnitOfWorkPropagation(value = MANDATORY)
    void createOrders(Map<Part,Integer> quantities, RuleSet rules);

    class Mixin implements SupplierOrderService
    {
        @Service
        RuleService ruleService;

        @Structure
        UnitOfWorkFactory uowf;

        @Override
        public void createOrders(Map<Part, Integer> quantities, RuleSet rules)
        {
            UnitOfWork uow = uowf.currentUnitOfWork();
            for (Map.Entry<Part, Integer> entry : quantities.entrySet())
            {
            }
        }
    }

}
