package ac.bali.bom.bootstrap;

import ac.bali.bom.products.*;
import org.apache.polygene.api.common.Visibility;
import org.apache.polygene.bootstrap.AssemblyException;
import org.apache.polygene.bootstrap.LayerAssembly;
import org.apache.polygene.bootstrap.ModuleAssembly;
import org.apache.polygene.bootstrap.layered.ModuleAssembler;

import static org.apache.polygene.api.common.Visibility.application;

public class ProductsModule
    implements ModuleAssembler
{
    public static final String NAME = "Products Module";

    @Override
    public ModuleAssembly assemble(LayerAssembly layer, ModuleAssembly module) throws AssemblyException
    {
        module.defaultServices();
        module.services(ProductsService.class).visibleIn(Visibility.application);
        module.services(BomReader.class).visibleIn(Visibility.application);
        module.entities(Product.class).visibleIn(application);
        module.values(Product.class).visibleIn(application);
        module.values(Bom.class,BomItem.class);
        return module;
    }
}
