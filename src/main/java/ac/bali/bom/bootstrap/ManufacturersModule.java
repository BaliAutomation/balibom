package ac.bali.bom.bootstrap;

import ac.bali.bom.manufacturers.Manufacturer;
import ac.bali.bom.manufacturers.ManufacturersService;
import org.apache.polygene.bootstrap.AssemblyException;
import org.apache.polygene.bootstrap.LayerAssembly;
import org.apache.polygene.bootstrap.ModuleAssembly;
import org.apache.polygene.bootstrap.layered.ModuleAssembler;

import static org.apache.polygene.api.common.Visibility.application;

public class ManufacturersModule
    implements ModuleAssembler
{
    public static final String NAME = "Manufacturers Module";

    @Override
    public ModuleAssembly assemble(LayerAssembly layer, ModuleAssembly module) throws AssemblyException
    {
        module.defaultServices();
        module.services(ManufacturersService.class).visibleIn(application);
        module.entities(Manufacturer.class).visibleIn(application);
        module.values(Manufacturer.class).visibleIn(application);
        return module;
    }
}
