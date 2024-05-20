package ac.bali.bom.bootstrap.model;

import ac.bali.bom.view.Deleter;
import org.apache.polygene.bootstrap.AssemblyException;
import org.apache.polygene.bootstrap.LayerAssembly;
import org.apache.polygene.bootstrap.ModuleAssembly;
import org.apache.polygene.bootstrap.layered.ModuleAssembler;

import static org.apache.polygene.api.common.Visibility.application;
import static org.apache.polygene.api.common.Visibility.layer;

public class ViewModelModule
        implements ModuleAssembler
{
    public static final String NAME = "View Model Module";

    @Override
    public ModuleAssembly assemble(LayerAssembly layerAssembly, ModuleAssembly module)
        throws AssemblyException
    {
        module.defaultServices();
        module.services(Deleter.class).visibleIn(layer);

        return module;
    }
}
