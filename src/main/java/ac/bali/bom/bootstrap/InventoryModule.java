package ac.bali.bom.bootstrap;

import ac.bali.bom.inventory.PartsInventory;
import ac.bali.bom.inventory.ProductsInventory;
import org.apache.polygene.api.common.Visibility;
import org.apache.polygene.bootstrap.AssemblyException;
import org.apache.polygene.bootstrap.LayerAssembly;
import org.apache.polygene.bootstrap.ModuleAssembly;
import org.apache.polygene.bootstrap.layered.ModuleAssembler;

import static org.apache.polygene.api.common.Visibility.application;

public class InventoryModule
        implements ModuleAssembler {
    public static final String NAME = "Inventory Module";

    @Override
    public ModuleAssembly assemble(LayerAssembly layer, ModuleAssembly module) throws AssemblyException {
        module.defaultServices();
        module.entities(PartsInventory.class, ProductsInventory.class).visibleIn(application);
        module.values(PartsInventory.class, ProductsInventory.class).visibleIn(application);
        return module;
    }
}
