package ac.bali.bom.bootstrap.infrastructure;

import org.apache.polygene.api.common.Visibility;
import org.apache.polygene.bootstrap.AssemblyException;
import org.apache.polygene.bootstrap.LayerAssembly;
import org.apache.polygene.bootstrap.ModuleAssembly;
import org.apache.polygene.bootstrap.layered.ModuleAssembler;
import org.apache.polygene.index.rdf.assembly.RdfIndexingAssembler;

public class IndexingModule
        implements ModuleAssembler {
    public static final String NAME = "Indexing Module";

    private final ModuleAssembly configModule;

    public IndexingModule(ModuleAssembly configModule)
    {
        this.configModule = configModule;
    }

    @Override
    public ModuleAssembly assemble(LayerAssembly layer, ModuleAssembly module) throws AssemblyException {
        module.defaultServices();
        new RdfIndexingAssembler ()
            .visibleIn(Visibility.application)
            .identifiedBy("rdf-indexer")
            .withConfig(configModule, Visibility.application)
            .assemble(module);
        return module;
    }
}
