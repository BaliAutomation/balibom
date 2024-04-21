package ac.bali.bom;

import ac.bali.bom.bootstrap.JavaFxModule;
import ac.bali.bom.bootstrap.Qi4jApplicationAssembler;
import ac.bali.bom.bootstrap.ViewLayer;
import ac.bali.bom.inventory.PartsInventory;
import ac.bali.bom.inventory.ProductsInventory;
import ac.bali.bom.manufacturers.Manufacturer;
import ac.bali.bom.order.Order;
import ac.bali.bom.parts.Part;
import ac.bali.bom.products.Product;
import ac.bali.bom.suppliers.Supplier;
import org.qi4j.library.javafx.ui.EntityPane;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import org.apache.polygene.api.object.ObjectFactory;
import org.apache.polygene.api.structure.Module;

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
        EntityPane<Product> productsPane = objectFactory.newObject(EntityPane.class, Product.class);
        EntityPane<Part> partsPane = objectFactory.newObject(EntityPane.class, Part.class);
        EntityPane<Order> ordersPane = objectFactory.newObject(EntityPane.class, Order.class);
        EntityPane<ProductsInventory> productsInventoryPane = objectFactory.newObject(EntityPane.class, ProductsInventory.class);
        EntityPane<PartsInventory> partsInventoryPane = objectFactory.newObject(EntityPane.class, PartsInventory.class);
        EntityPane<Manufacturer> manufacturersPane = objectFactory.newObject(EntityPane.class, Manufacturer.class);
        EntityPane<Supplier> suppliersPane = objectFactory.newObject(EntityPane.class, Supplier.class);
        TabPane tabs = setupNavigationTabs(productsPane, partsPane, ordersPane, productsInventoryPane, partsInventoryPane, manufacturersPane, suppliersPane);

        productsPane.loadAll();
        Scene mainScene = new Scene(tabs, 1000, 600);
        stage.setScene(mainScene);
        stage.show();
    }

    private static TabPane setupNavigationTabs(EntityPane<Product> productsPane, EntityPane<Part> partsPane, EntityPane<Order> ordersPane, EntityPane<ProductsInventory> productsInventoryPane, EntityPane<PartsInventory> partsInventoryPane, EntityPane<Manufacturer> manufacturersPane, EntityPane<Supplier> suppliersPane)
    {
        TabPane tabs = new TabPane();
        createTab(tabs, "Products", productsPane);
        createTab(tabs, "Parts", partsPane);
        createTab(tabs, "Orders", ordersPane);
        createTab(tabs, "Part Inventory", partsInventoryPane);
        createTab(tabs, "Product Inventory", productsInventoryPane);
        createTab(tabs, "Manufacturers", manufacturersPane);
        createTab(tabs, "Suppliers", suppliersPane);
        return tabs;
    }

    private static void createTab(TabPane tabs, String title, EntityPane<?> pane)
    {
        Tab tab = new Tab(title, pane);
        tab.setOnSelectionChanged(evt -> pane.loadAll());
        tabs.getTabs().add(tab);
        pane.prefWidthProperty().bind(tabs.widthProperty());
        pane.prefHeightProperty().bind(tabs.heightProperty());
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
