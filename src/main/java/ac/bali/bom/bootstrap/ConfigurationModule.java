package ac.bali.bom.bootstrap;

import org.apache.polygene.bootstrap.AssemblyException;
import org.apache.polygene.bootstrap.LayerAssembly;
import org.apache.polygene.bootstrap.ModuleAssembly;
import org.apache.polygene.bootstrap.layered.ModuleAssembler;
import org.apache.polygene.entitystore.memory.assembly.MemoryEntityStoreAssembler;
import org.apache.polygene.library.fileconfig.FileConfigurationAssembler;
import org.apache.polygene.library.rdf.repository.NativeConfiguration;
import org.apache.polygene.library.uowfile.bootstrap.UoWFileAssembler;

import static org.apache.polygene.api.common.Visibility.application;

public class ConfigurationModule
        implements ModuleAssembler {

    public static final String NAME = "Configuration Module";

    @Override
    public ModuleAssembly assemble(LayerAssembly layer, ModuleAssembly module) throws AssemblyException {
        module.defaultServices();
        module.configurations(NativeConfiguration.class).visibleIn(application);
        new MemoryEntityStoreAssembler().assemble(module);
        new FileConfigurationAssembler().visibleIn(application).assemble(module);
        new UoWFileAssembler().visibleIn(application).assemble(module);
        return module;
    }
}
