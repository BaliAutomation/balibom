package ac.bali.bom.bootstrap;

import ac.bali.bom.manufacturers.Manufacturer;
import ac.bali.bom.manufacturers.ManufacturersService;
import ac.bali.bom.parts.Part;
import ac.bali.bom.parts.PartsService;
import ac.bali.bom.parts.Price;
import ac.bali.bom.parts.Supply;
import ac.bali.bom.suppliers.Supplier;
import ac.bali.bom.suppliers.SuppliersService;
import org.apache.polygene.bootstrap.AssemblyException;
import org.apache.polygene.bootstrap.LayerAssembly;
import org.apache.polygene.bootstrap.ModuleAssembly;
import org.apache.polygene.bootstrap.layered.ModuleAssembler;

import static org.apache.polygene.api.common.Visibility.application;

public class SuppliersModule
    implements ModuleAssembler
{
    public static final String NAME = "Suppliers Module";

    @Override
    public ModuleAssembly assemble(LayerAssembly layer, ModuleAssembly module) throws AssemblyException
    {
        module.defaultServices();

        new LcscAssembler().assemble(module);

        module.services(SuppliersService.class).instantiateOnStartup().visibleIn(application);
        module.entities(Supplier.class).visibleIn(application);
        module.values(Supplier.class).visibleIn(application);
        return module;
    }
}
