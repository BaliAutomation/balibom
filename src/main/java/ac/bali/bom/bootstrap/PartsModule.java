package ac.bali.bom.bootstrap;

import ac.bali.bom.parts.*;
import ac.bali.bom.products.BomReader;
import org.apache.polygene.api.common.Visibility;
import org.apache.polygene.bootstrap.AssemblyException;
import org.apache.polygene.bootstrap.LayerAssembly;
import org.apache.polygene.bootstrap.ModuleAssembly;
import org.apache.polygene.bootstrap.layered.ModuleAssembler;

import static org.apache.polygene.api.common.Visibility.application;

public class PartsModule
    implements ModuleAssembler
{
    public static final String NAME = "Parts Module";

    @Override
    public ModuleAssembly assemble(LayerAssembly layer, ModuleAssembly module) throws AssemblyException
    {
        module.defaultServices();
        module.services(ManufacturersService.class).visibleIn(application);
        module.services(PartsService.class).visibleIn(application);
        module.services(BomReader.class);
        module.entities(Manufacturer.class, Part.class, Supplier.class).visibleIn(application);
        module.values(Manufacturer.class, Part.class, Supplier.class).visibleIn(application);
        module.values(Supply.class);
        module.transients(Manufacturer.class);
        return module;
    }
}
