package ac.bali.bom.bootstrap.model;

import ac.bali.bom.bootstrap.ModelLayer;
import ac.bali.bom.parts.Part;
import ac.bali.bom.parts.PartsService;
import ac.bali.bom.parts.Price;
import ac.bali.bom.suppliers.Supply;
import ac.bali.bom.view.ResolveParts;
import org.apache.polygene.api.identity.HasIdentity;
import org.apache.polygene.bootstrap.AssemblyException;
import org.apache.polygene.bootstrap.LayerAssembly;
import org.apache.polygene.bootstrap.ModuleAssembly;
import org.apache.polygene.bootstrap.layered.ModuleAssembler;

import static org.apache.polygene.api.common.Visibility.application;
import static org.apache.polygene.api.common.Visibility.layer;

public class PartsModule
    implements ModuleAssembler
{
    public static final String NAME = "Parts Module";

    @Override
    public ModuleAssembly assemble(LayerAssembly layer, ModuleAssembly module) throws AssemblyException
    {
        module.defaultServices();
        module.services(PartsService.class).visibleIn(application);
        module.services(ResolveParts.class).visibleIn(application);

        module.entities(Part.class).visibleIn(application);

        module.values(Supply.class, Price.class).visibleIn(application);

        return module;
    }
}