package ac.bali.bom.bootstrap;

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
        new DigikeyAssembler().assemble(module);
        new Oauth2Assembler().assemble(module);

        module.services(SuppliersService.class).instantiateOnStartup().visibleIn(application);
        module.entities(Supplier.class).visibleIn(application);
        module.values(Supplier.class).visibleIn(application);
        return module;
    }
}
