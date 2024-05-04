package ac.bali.bom.bootstrap.model;

import ac.bali.bom.bootstrap.ModelLayer;
import ac.bali.bom.order.Order;
import ac.bali.bom.order.OrderItem;
import ac.bali.bom.order.OrderService;
import org.apache.polygene.api.common.Visibility;
import org.apache.polygene.api.identity.HasIdentity;
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
        module.entities(Order.class).visibleIn(application);
        module.services( OrderService.class ).visibleIn(Visibility.application);
        return module;
    }
}
