package ac.bali.bom.bootstrap;

import org.apache.polygene.bootstrap.AssemblyException;
import org.apache.polygene.bootstrap.LayerAssembly;
import org.apache.polygene.bootstrap.ModuleAssembly;
import org.apache.polygene.bootstrap.layered.LayeredLayerAssembler;

public class ConfigurationLayer extends LayeredLayerAssembler {
    public static final String NAME = "Configuration Layer";

    private ModuleAssembly module;

    @Override
    public LayerAssembly assemble(LayerAssembly layer) throws AssemblyException {
        module = createModule(layer, ConfigurationModule.class);
        return layer;
    }
}
