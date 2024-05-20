package ac.bali.bom.bootstrap;

import ac.bali.bom.bootstrap.model.CustomersModule;
import ac.bali.bom.bootstrap.model.InventoryModule;
import ac.bali.bom.bootstrap.model.IssuesModule;
import ac.bali.bom.bootstrap.model.JavaFxModule;
import ac.bali.bom.bootstrap.model.JobsModule;
import ac.bali.bom.bootstrap.model.ManufacturersModule;
import ac.bali.bom.bootstrap.model.OrderModule;
import ac.bali.bom.bootstrap.model.PartsModule;
import ac.bali.bom.bootstrap.model.ProductsModule;
import ac.bali.bom.bootstrap.model.SuppliersModule;
import ac.bali.bom.bootstrap.model.ViewModelModule;
import java.lang.annotation.Annotation;
import org.apache.polygene.bootstrap.AssemblyException;
import org.apache.polygene.bootstrap.LayerAssembly;
import org.apache.polygene.bootstrap.layered.LayeredLayerAssembler;
import org.qi4j.library.javafx.support.ObservableAssociationConcern;
import org.qi4j.library.javafx.support.ObservablePropertyConcern;
import org.qi4j.library.javafx.support.Order;

import static org.apache.polygene.api.common.Visibility.application;

public class ModelLayer extends LayeredLayerAssembler
{
    public static final String NAME = "Model Layer";
    public static final Order IDENTITY_ORDER = new Order()
    {
        @Override
        public int value()
        {
            return 1;
        }

        @Override
        public Class<? extends Annotation> annotationType()
        {
            return Order.class;
        }
    };

    @Override
    public LayerAssembly assemble(LayerAssembly layer) throws AssemblyException
    {
        createModule(layer, JavaFxModule.class);

        createModule(layer, CustomersModule.class);
        createModule(layer, InventoryModule.class);
        createModule(layer, IssuesModule.class);
        createModule(layer, JobsModule.class);
        createModule(layer, ManufacturersModule.class);
        createModule(layer, OrderModule.class);
        createModule(layer, PartsModule.class);
        createModule(layer, ProductsModule.class);
        createModule(layer, SuppliersModule.class);
        createModule(layer, ViewModelModule.class);
        Annotation annot = new Annotation()
        {
            @Override
            public Class<? extends Annotation> annotationType()
            {
                return Order.class;
            }
        };
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
