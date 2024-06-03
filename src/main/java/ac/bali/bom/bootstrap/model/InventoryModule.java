package ac.bali.bom.bootstrap.model;

import ac.bali.bom.inventory.parts.PartsInventory;
import ac.bali.bom.inventory.parts.PartsInventoryService;
import ac.bali.bom.inventory.products.ProductsInventory;
import ac.bali.bom.inventory.products.ProductsInventoryService;
import org.apache.polygene.bootstrap.AssemblyException;
import org.apache.polygene.bootstrap.LayerAssembly;
import org.apache.polygene.bootstrap.ModuleAssembly;
import org.apache.polygene.bootstrap.layered.ModuleAssembler;

import static org.apache.polygene.api.common.Visibility.application;

public class InventoryModule
    implements ModuleAssembler
{
    public static final String NAME = "Inventory Module";

    @Override
    public ModuleAssembly assemble(LayerAssembly layer, ModuleAssembly module) throws AssemblyException
    {
        module.defaultServices();
        module.entities(PartsInventory.class, ProductsInventory.class).visibleIn(application);
        module.services(PartsInventoryService.class).visibleIn(application);
        module.services(ProductsInventoryService.class).visibleIn(application);
        return module;
    }
}
