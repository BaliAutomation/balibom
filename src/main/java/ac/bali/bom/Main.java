package ac.bali.bom;

import ac.bali.bom.bootstrap.JavaFxModule;
import ac.bali.bom.bootstrap.Qi4jApplicationAssembler;
import ac.bali.bom.bootstrap.ViewLayer;
import ac.bali.bom.inventory.PartsInventory;
import ac.bali.bom.inventory.ProductsInventory;
import ac.bali.bom.jobs.Job;
import ac.bali.bom.manufacturers.Manufacturer;
import ac.bali.bom.order.Order;
import ac.bali.bom.parts.Part;
import ac.bali.bom.products.Product;
import ac.bali.bom.suppliers.Supplier;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import org.apache.polygene.api.object.ObjectFactory;
import org.apache.polygene.api.structure.Module;
import org.qi4j.library.javafx.ui.EntityPane;

import static org.apache.polygene.api.structure.Application.Mode.development;

public class Main extends Application
{
    private Qi4jApplicationAssembler qi4jApplication;

    @SuppressWarnings("unchecked")
    @Override
    public void start(Stage stage) throws Exception
    {
        System.out.println("java version: " + System.getProperty("java.version"));
        System.out.println("javafx.version: " + System.getProperty("javafx.version"));

        qi4jApplication = new Qi4jApplicationAssembler("Bill Of Materials", "1.0", development);
        qi4jApplication.initialize();
        qi4jApplication.start();
        Module module = qi4jApplication.application().findModule(ViewLayer.NAME, JavaFxModule.NAME);
        ObjectFactory objectFactory = module.objectFactory();

        stage.setTitle("Bill Of Materials");
        EntityPane<Product> productsPane = objectFactory.newObject(EntityPane.class, Product.class, "Products");
        EntityPane<Part> jobsPane = objectFactory.newObject(EntityPane.class, Job.class, "Jobs");
        EntityPane<Part> partsPane = objectFactory.newObject(EntityPane.class, Part.class, "Parts");
        EntityPane<Order> ordersPane = objectFactory.newObject(EntityPane.class, Order.class, "Orders");
        EntityPane<ProductsInventory> productsInventoryPane = objectFactory.newObject(EntityPane.class, ProductsInventory.class, "Product Inventory");
        EntityPane<PartsInventory> partsInventoryPane = objectFactory.newObject(EntityPane.class, PartsInventory.class, "Parts Inventory");
        EntityPane<Manufacturer> manufacturersPane = objectFactory.newObject(EntityPane.class, Manufacturer.class, "Manufacturers");
        EntityPane<Supplier> suppliersPane = objectFactory.newObject(EntityPane.class, Supplier.class, "Suppliers");
        TabPane tabs = setupNavigationTabs(productsPane, jobsPane, partsPane, ordersPane, productsInventoryPane, partsInventoryPane, manufacturersPane, suppliersPane);

        productsPane.loadAll();
        Scene mainScene = new Scene(tabs, 1200, 800);
        stage.setScene(mainScene);
        stage.show();
    }

    private static TabPane setupNavigationTabs(EntityPane<?>... panes)
    {
        TabPane tabs = new TabPane();
        for( EntityPane<?> pane : panes)
        {
            String title = pane.title();
            Tab tab = new Tab(title, pane);
            tab.setOnSelectionChanged(evt -> pane.loadAll());
            tab.setClosable(false);
            tabs.getTabs().add(tab);
            pane.prefWidthProperty().bind(tabs.widthProperty());
            pane.prefHeightProperty().bind(tabs.heightProperty());
        }
        return tabs;
    }

    @Override
    public void stop() throws Exception
    {
        super.stop();
        qi4jApplication.stop();
    }

    public static void main(String[] args)
    {
        launch();
    }
}
