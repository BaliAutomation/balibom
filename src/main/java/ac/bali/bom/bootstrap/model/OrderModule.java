package ac.bali.bom.bootstrap.model;

import ac.bali.bom.order.Order;
import ac.bali.bom.order.OrderItem;
import ac.bali.bom.order.OrderService;
import ac.bali.bom.rules.RuleEntity;
import ac.bali.bom.rules.RuleService;
import ac.bali.bom.rules.RuleSet;
import ac.bali.bom.rules.RuleSetService;
import org.apache.polygene.bootstrap.AssemblyException;
import org.apache.polygene.bootstrap.LayerAssembly;
import org.apache.polygene.bootstrap.ModuleAssembly;
import org.apache.polygene.bootstrap.layered.ModuleAssembler;

import static org.apache.polygene.api.common.Visibility.application;

public class OrderModule
        implements ModuleAssembler {
    public static final String NAME = "Order Module";

    @Override
    public ModuleAssembly assemble(LayerAssembly layer, ModuleAssembly module) throws AssemblyException {
        module.defaultServices();

        module.values(OrderItem.class).visibleIn(application);

        module.entities(Order.class).visibleIn(application);
        module.entities(RuleEntity.class).visibleIn(application);
        module.entities(RuleSet.class).visibleIn(application);

        module.services( OrderService.class ).visibleIn(application);
        module.services( RuleService.class ).visibleIn(application);
        module.services( RuleSetService.class ).visibleIn(application);
        return module;
    }
}
