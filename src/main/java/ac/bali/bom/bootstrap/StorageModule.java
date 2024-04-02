package ac.bali.bom.bootstrap;

import org.apache.polygene.api.common.Visibility;
import org.apache.polygene.bootstrap.AssemblyException;
import org.apache.polygene.bootstrap.LayerAssembly;
import org.apache.polygene.bootstrap.ModuleAssembly;
import org.apache.polygene.bootstrap.layered.ModuleAssembler;
import org.apache.polygene.entitystore.file.assembly.FileEntityStoreAssembler;

public class StorageModule
    implements ModuleAssembler
{
    public static final String NAME = "Storage Module";

    private final ModuleAssembly configModule;

    public StorageModule(ModuleAssembly configModule)
    {
        this.configModule = configModule;
    }

    @Override
    public ModuleAssembly assemble(LayerAssembly layer, ModuleAssembly module) throws AssemblyException
    {
        module.defaultServices();
        new FileEntityStoreAssembler()
            .visibleIn(Visibility.application)
            .identifiedBy("file-es")
            .withConfig(configModule, Visibility.application)
            .assemble(module);
        return module;
    }
}
