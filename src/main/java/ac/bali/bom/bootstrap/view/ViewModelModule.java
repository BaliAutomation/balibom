package ac.bali.bom.bootstrap.view;

import org.apache.polygene.bootstrap.AssemblyException;
import org.apache.polygene.bootstrap.LayerAssembly;
import org.apache.polygene.bootstrap.ModuleAssembly;
import org.apache.polygene.bootstrap.layered.ModuleAssembler;

public class ViewModelModule
        implements ModuleAssembler
{
    public static final String NAME = "View Model Module";

    @Override
    public ModuleAssembly assemble(LayerAssembly layerAssembly, ModuleAssembly module)
        throws AssemblyException
    {
        module.defaultServices();
        return module;
    }
}
