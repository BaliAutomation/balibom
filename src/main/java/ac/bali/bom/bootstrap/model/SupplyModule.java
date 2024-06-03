package ac.bali.bom.bootstrap.model;

import ac.bali.bom.supply.Supplier;
import ac.bali.bom.supply.SuppliersService;
import ac.bali.bom.supply.order.SupplierOrder;
import ac.bali.bom.supply.order.SupplierOrderItem;
import ac.bali.bom.supply.order.SupplierOrderService;
import ac.bali.bom.supply.rules.RuleService;
import ac.bali.bom.supply.rules.RuleSet;
import ac.bali.bom.supply.rules.RuleSetService;
import ac.bali.bom.supply.rules.SupplyRule;
import org.apache.polygene.bootstrap.AssemblyException;
import org.apache.polygene.bootstrap.LayerAssembly;
import org.apache.polygene.bootstrap.ModuleAssembly;
import org.apache.polygene.bootstrap.layered.ModuleAssembler;
import org.qi4j.library.crudui.javafx.support.ObservablePropertyConcern;

import static org.apache.polygene.api.common.Visibility.application;

public class SupplyModule
    implements ModuleAssembler
{
    public static final String NAME = "Suppliers Module";

    @Override
    public ModuleAssembly assemble(LayerAssembly layer, ModuleAssembly module)
        throws AssemblyException
    {
        module.defaultServices();

        // Suppliers
        new LcscAssembler().assemble(module);
        new DigikeyAssembler().assemble(module);
        new ManualSupplierAssembler().assemble(module);
        new MouserAssembler().assemble(module);
        new Oauth2Assembler().assemble(module);
        new ApiKeyAssembler().assemble(module);

        module.services(SuppliersService.class).instantiateOnStartup().visibleIn(application);
        module.entities(Supplier.class)
            .withConcerns(ObservablePropertyConcern.class)
        ;

        // Orders
        module.values(SupplierOrderItem.class).visibleIn(application);

        module.entities(SupplierOrder.class).visibleIn(application);
        module.entities(SupplyRule.class).visibleIn(application);
        module.entities(RuleSet.class).visibleIn(application);

        module.services( SupplierOrderService.class ).visibleIn(application);
        module.services( RuleService.class ).visibleIn(application);
        module.services( RuleSetService.class ).visibleIn(application);

        return module;
    }
}
