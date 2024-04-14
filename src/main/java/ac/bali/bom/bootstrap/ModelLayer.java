package ac.bali.bom.bootstrap;

import org.apache.polygene.bootstrap.AssemblyException;
import org.apache.polygene.bootstrap.LayerAssembly;
import org.apache.polygene.bootstrap.layered.LayeredLayerAssembler;

public class ModelLayer extends LayeredLayerAssembler
{
    public static final String NAME = "Model Layer";

    @Override
    public LayerAssembly assemble(LayerAssembly layer) throws AssemblyException
    {
        createModule(layer, InventoryModule.class);
        createModule(layer, OrderModule.class);
        createModule(layer, ManufacturersModule.class);
        createModule(layer, PartsModule.class);
        createModule(layer, ProductsModule.class);
        createModule(layer, SuppliersModule.class);
        createModule(layer, ViewModelModule.class);
        return layer;
    }
}
