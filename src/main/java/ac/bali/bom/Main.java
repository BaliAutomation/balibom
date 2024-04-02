package ac.bali.bom;

import ac.bali.bom.bootstrap.JavaFxModule;
import ac.bali.bom.bootstrap.Qi4jApplicationAssembler;
import ac.bali.bom.bootstrap.ViewLayer;
import ac.bali.bom.inventory.PartsInventory;
import ac.bali.bom.inventory.ProductsInventory;
import ac.bali.bom.order.Order;
import ac.bali.bom.parts.Manufacturer;
import ac.bali.bom.parts.Part;
import ac.bali.bom.products.Product;
import ac.bali.bom.ui.EntityPane;
import javafx.application.Application;
import javafx.application.Platform;
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
        TabPane tabs = setupNavigationTabs(productsPane, partsPane, ordersPane, productsInventoryPane, partsInventoryPane, manufacturersPane);

        productsPane.loadAll();
        Scene mainScene = new Scene(tabs, 1000, 600);
        stage.setScene(mainScene);
        stage.show();
    }

    private static TabPane setupNavigationTabs(EntityPane<Product> productsPane, EntityPane<Part> partsPane, EntityPane<Order> ordersPane, EntityPane<ProductsInventory> productsInventoryPane, EntityPane<PartsInventory> partsInventoryPane, EntityPane<Manufacturer> manufacturersPane)
    {
        Tab products = new Tab("Products", productsPane);
        Tab parts = new Tab("Parts", partsPane);
        Tab orders = new Tab("Orders", ordersPane);
        Tab invParts = new Tab("Part Inventory", partsInventoryPane);
        Tab invProducts = new Tab("Product Inventory", productsInventoryPane);
        Tab manufacturers = new Tab("Manufacturers", manufacturersPane);
        TabPane tabs = new TabPane(products, parts, orders, invProducts, invParts, manufacturers);
        products.setOnSelectionChanged(evt -> productsPane.loadAll());
        parts.setOnSelectionChanged(evt -> partsPane.loadAll());
        orders.setOnSelectionChanged(evt -> ordersPane.loadAll());
        invParts.setOnSelectionChanged(evt -> partsInventoryPane.loadAll());
        invProducts.setOnSelectionChanged(evt -> productsInventoryPane.loadAll());
        manufacturers.setOnSelectionChanged(evt -> manufacturersPane.loadAll());

        productsPane.prefWidthProperty().bind(tabs.widthProperty());
        productsPane.prefHeightProperty().bind(tabs.heightProperty());
        partsPane.prefWidthProperty().bind(tabs.widthProperty());
        partsPane.prefHeightProperty().bind(tabs.heightProperty());
        ordersPane.prefWidthProperty().bind(tabs.widthProperty());
        ordersPane.prefHeightProperty().bind(tabs.heightProperty());
        partsInventoryPane.prefWidthProperty().bind(tabs.widthProperty());
        productsInventoryPane.prefHeightProperty().bind(tabs.heightProperty());
        manufacturersPane.prefWidthProperty().bind(tabs.widthProperty());
        manufacturersPane.prefHeightProperty().bind(tabs.heightProperty());
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
