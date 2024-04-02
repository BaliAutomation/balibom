package ac.bali.bom.bootstrap;

import org.apache.polygene.bootstrap.AssemblyException;
import org.apache.polygene.bootstrap.LayerAssembly;
import org.apache.polygene.bootstrap.ModuleAssembly;
import org.apache.polygene.bootstrap.layered.LayerAssembler;
import org.apache.polygene.bootstrap.layered.LayeredLayerAssembler;

public class InfrastructureLayer extends LayeredLayerAssembler
        implements LayerAssembler {

    public static final String NAME = "Infrastructure Layer";

    @Override
    public LayerAssembly assemble(LayerAssembly layer) throws AssemblyException {
        ModuleAssembly configModule = layer.application().layer("Configuration Layer").module("Configuration Module");
        createModule( layer, StorageModule.class, configModule);
        createModule(layer, IndexingModule.class);
        return layer;
    }
}
