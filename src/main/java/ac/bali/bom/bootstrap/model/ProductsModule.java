package ac.bali.bom.bootstrap.model;

import ac.bali.bom.products.Bom;
import ac.bali.bom.products.BomItem;
import ac.bali.bom.products.BomReader;
import ac.bali.bom.products.CostItem;
import ac.bali.bom.products.CostReport;
import ac.bali.bom.products.PartQuantity;
import ac.bali.bom.products.Product;
import ac.bali.bom.products.ProductsService;
import org.apache.polygene.bootstrap.AssemblyException;
import org.apache.polygene.bootstrap.LayerAssembly;
import org.apache.polygene.bootstrap.ModuleAssembly;
import org.apache.polygene.bootstrap.layered.ModuleAssembler;
import org.apache.polygene.library.fileconfig.FileConfigurationService;
import org.qi4j.library.crudui.javafx.support.ObservableAssociationConcern;
import org.qi4j.library.crudui.javafx.support.ObservableManyAssociationConcern;
import org.qi4j.library.crudui.javafx.support.ObservableNamedAssociationConcern;
import org.qi4j.library.crudui.javafx.support.ObservablePropertyConcern;

import static org.apache.polygene.api.common.Visibility.application;

public class ProductsModule
    implements ModuleAssembler
{
    public static final String NAME = "Products Module";

    @Override
    public ModuleAssembly assemble(LayerAssembly layer, ModuleAssembly module) throws AssemblyException
    {
        module.defaultServices();
        module.services(FileConfigurationService.class);
        module.services(ProductsService.class).visibleIn(application);
        module.services(BomReader.class).visibleIn(application);

        module.entities(Product.class, Bom.class, BomItem.class)
            .withConcerns(
                ObservablePropertyConcern.class,
                ObservableManyAssociationConcern.class,
                ObservableAssociationConcern.class,
                ObservableNamedAssociationConcern.class
            )
            .visibleIn(application);
        module.values(PartQuantity.class).visibleIn(application);
        module.values(CostReport.class, CostItem.class).visibleIn(application);
        return module;
    }
}
