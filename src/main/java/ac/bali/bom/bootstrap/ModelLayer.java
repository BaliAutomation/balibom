package ac.bali.bom.bootstrap;

import ac.bali.bom.bootstrap.model.CustomersModule;
import ac.bali.bom.bootstrap.model.InventoryModule;
import ac.bali.bom.bootstrap.model.IssuesModule;
import ac.bali.bom.bootstrap.model.JavaFxModule;
import ac.bali.bom.bootstrap.model.JobsModule;
import ac.bali.bom.bootstrap.model.ManufacturersModule;
import ac.bali.bom.bootstrap.model.PartsModule;
import ac.bali.bom.bootstrap.model.ProductsModule;
import ac.bali.bom.bootstrap.model.SupplyModule;
import ac.bali.bom.bootstrap.model.ViewModelModule;
import org.apache.polygene.bootstrap.AssemblyException;
import org.apache.polygene.bootstrap.LayerAssembly;
import org.apache.polygene.bootstrap.layered.LayeredLayerAssembler;
import org.qi4j.library.crudui.javafx.support.ObservableAssociationConcern;
import org.qi4j.library.crudui.javafx.support.ObservablePropertyConcern;

import static org.apache.polygene.api.common.Visibility.application;

public class ModelLayer extends LayeredLayerAssembler
{
    public static final String NAME = "Model Layer";

    @Override
    public LayerAssembly assemble(LayerAssembly layer) throws AssemblyException
    {
        createModule(layer, JavaFxModule.class);

        createModule(layer, CustomersModule.class);
        createModule(layer, InventoryModule.class);
        createModule(layer, IssuesModule.class);
        createModule(layer, JobsModule.class);
        createModule(layer, ManufacturersModule.class);
        createModule(layer, PartsModule.class);
        createModule(layer, ProductsModule.class);
        createModule(layer, SupplyModule.class);
        createModule(layer, ViewModelModule.class);
        layer.entities(e -> true)
            .withConcerns(ObservablePropertyConcern.class)
            .withConcerns(ObservableAssociationConcern.class)
            .visibleIn(application);
        layer.values(e -> true)
            .withConcerns(ObservablePropertyConcern.class)
            .withConcerns(ObservableAssociationConcern.class)
            .visibleIn(application);

        return layer;
    }
}
