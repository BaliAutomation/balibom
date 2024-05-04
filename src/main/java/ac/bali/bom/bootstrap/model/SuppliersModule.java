package ac.bali.bom.bootstrap.model;

import ac.bali.bom.bootstrap.ModelLayer;
import ac.bali.bom.suppliers.Supplier;
import ac.bali.bom.suppliers.SuppliersService;
import javafx.beans.value.ObservableValue;
import org.apache.polygene.api.identity.HasIdentity;
import org.apache.polygene.bootstrap.AssemblyException;
import org.apache.polygene.bootstrap.LayerAssembly;
import org.apache.polygene.bootstrap.ModuleAssembly;
import org.apache.polygene.bootstrap.layered.ModuleAssembler;
import org.qi4j.library.javafx.support.ObservablePropertyConcern;
import org.qi4j.library.javafx.support.ObservablePropertyWrapper;

import static org.apache.polygene.api.common.Visibility.application;

public class SuppliersModule
    implements ModuleAssembler
{
    public static final String NAME = "Suppliers Module";

    @Override
    public ModuleAssembly assemble(LayerAssembly layer, ModuleAssembly module)
        throws AssemblyException
    {
        module.defaultServices();

        new LcscAssembler().assemble(module);
        new DigikeyAssembler().assemble(module);
        new MouserAssembler().assemble(module);
        new Oauth2Assembler().assemble(module);
        new ApiKeyAssembler().assemble(module);

        module.services(SuppliersService.class).instantiateOnStartup().visibleIn(application);
        module.entities(Supplier.class);
        return module;
    }
}
