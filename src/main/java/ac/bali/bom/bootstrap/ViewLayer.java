package ac.bali.bom.bootstrap;

import org.apache.polygene.bootstrap.AssemblyException;
import org.apache.polygene.bootstrap.LayerAssembly;
import org.apache.polygene.bootstrap.layered.LayerAssembler;
import org.apache.polygene.bootstrap.layered.LayeredLayerAssembler;

public class ViewLayer extends LayeredLayerAssembler
        implements LayerAssembler {
    public static final String NAME = "View Layer";

    @Override
    public LayerAssembly assemble(LayerAssembly layer) throws AssemblyException {
        createModule(layer, JavaFxModule.class);
        return layer;
    }
}
