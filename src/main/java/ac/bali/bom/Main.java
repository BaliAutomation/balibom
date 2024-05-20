package ac.bali.bom;

import ac.bali.bom.bootstrap.ModelLayer;
import ac.bali.bom.bootstrap.Qi4jApplicationAssembler;
import ac.bali.bom.bootstrap.model.JavaFxModule;
import ac.bali.bom.customers.Customer;
import ac.bali.bom.inventory.PartsInventory;
import ac.bali.bom.inventory.ProductsInventory;
import ac.bali.bom.jobs.Job;
import ac.bali.bom.manufacturers.Manufacturer;
import ac.bali.bom.order.Order;
import ac.bali.bom.parts.Part;
import ac.bali.bom.products.Product;
import ac.bali.bom.suppliers.Supplier;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.apache.polygene.api.object.ObjectFactory;
import org.apache.polygene.api.structure.Module;
import org.qi4j.library.javafx.ui.EntityPane;

import static org.apache.polygene.api.structure.Application.Mode.production;

public class Main extends Application
{
    private Qi4jApplicationAssembler qi4jApplication;

    @SuppressWarnings("unchecked")
    @Override
    public void start(Stage stage) throws Exception
    {
        System.out.println("java version: " + System.getProperty("java.version"));
        System.out.println("javafx.version: " + System.getProperty("javafx.version"));
        setupLogging();
        qi4jApplication = new Qi4jApplicationAssembler("Bill Of Materials", "1.0", production);
        qi4jApplication.initialize();
        qi4jApplication.start();
        Module module = qi4jApplication.application().findModule(ModelLayer.NAME, JavaFxModule.NAME);
        module.unitOfWorkFactory().newUnitOfWork();
        ObjectFactory objectFactory = module.objectFactory();

        stage.setTitle("Bill Of Materials");
        EntityPane<Product> productsPane = objectFactory.newObject(EntityPane.class, Product.class, "Products");
        EntityPane<Part> jobsPane = objectFactory.newObject(EntityPane.class, Job.class, "Jobs");
        EntityPane<Part> partsPane = objectFactory.newObject(EntityPane.class, Part.class, "Parts");
        EntityPane<Order> ordersPane = objectFactory.newObject(EntityPane.class, Order.class, "Orders");
        EntityPane<ProductsInventory> productsInventoryPane = objectFactory.newObject(EntityPane.class, ProductsInventory.class, "Product Inventory");
        EntityPane<PartsInventory> partsInventoryPane = objectFactory.newObject(EntityPane.class, PartsInventory.class, "Parts Inventory");
        EntityPane<Customer> customersPane = objectFactory.newObject(EntityPane.class, Customer.class, "Customers");
        EntityPane<Manufacturer> manufacturersPane = objectFactory.newObject(EntityPane.class, Manufacturer.class, "Manufacturers");
        EntityPane<Supplier> suppliersPane = objectFactory.newObject(EntityPane.class, Supplier.class, "Suppliers");
        TabPane tabs = setupNavigationTabs(productsPane, jobsPane, partsPane, ordersPane, productsInventoryPane, partsInventoryPane, customersPane, manufacturersPane, suppliersPane);
        Scene mainScene = new Scene(tabs, 1200, 800);
//        mainScene.addPreLayoutPulseListener(() -> module.unitOfWorkFactory().newUnitOfWork());
//        mainScene.addPostLayoutPulseListener(() -> module.unitOfWorkFactory().currentUnitOfWork().complete());
        stage.setScene(mainScene);
        stage.show();
    }

    private static TabPane setupNavigationTabs(EntityPane<?>... panes)
    {
        TabPane tabs = new TabPane();
        for (EntityPane<?> pane : panes)
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

    private void setupLogging()
    {
        File configFile = new File("logging.properties").getAbsoluteFile();
        System.setProperty("java.util.logging.config.file", configFile.getPath());
        Logger httpLogger = Logger.getLogger("org.apache.http.client.protocol.ResponseProcessCookies");
        httpLogger.setLevel(Level.SEVERE);
        httpLogger.setFilter(record -> false);
    }

    public static class Sample extends Application
    {

        private final TableView<Person> table = new TableView<Person>();
        private final ObservableList<Person> data =
            FXCollections.observableArrayList(
                new Person("Jacob", "Smith", "jacob.smith@example.com"),
                new Person("Isabella", "Johnson", "isabella.johnson@example.com"),
                new Person("Ethan", "Williams", "ethan.williams@example.com"),
                new Person("Emma", "Jones", "emma.jones@example.com"),
                new Person("Michael", "Brown", "michael.brown@example.com"));
        final HBox hb = new HBox();

        public static void main(String[] args)
        {
            launch(args);
        }

        @Override
        public void start(Stage stage)
        {
            Scene scene = new Scene(new Group());
            stage.setTitle("Table View Sample");
            stage.setWidth(450);
            stage.setHeight(550);

            final Label label = new Label("Address Book");
            label.setFont(new Font("Arial", 20));

            table.setEditable(true);

            TableColumn firstNameCol = new TableColumn("First Name");
            firstNameCol.setMinWidth(100);
            firstNameCol.setCellValueFactory(
                new PropertyValueFactory<Person, String>("firstName"));
            firstNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
            firstNameCol.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Person, String>>()
                {
                    @Override
                    public void handle(TableColumn.CellEditEvent<Person, String> t)
                    {
                        t.getTableView().getItems().get(
                            t.getTablePosition().getRow()).setFirstName(t.getNewValue());
                    }
                }
            );


            TableColumn lastNameCol = new TableColumn("Last Name");
            lastNameCol.setMinWidth(100);
            lastNameCol.setCellValueFactory(
                new PropertyValueFactory<Person, String>("lastName"));
            lastNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
            lastNameCol.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Person, String>>()
                {
                    @Override
                    public void handle(TableColumn.CellEditEvent<Person, String> t)
                    {
                        t.getTableView().getItems().get(
                            t.getTablePosition().getRow()).setLastName(t.getNewValue());
                    }
                }
            );

            TableColumn emailCol = new TableColumn("Email");
            emailCol.setMinWidth(200);
            emailCol.setCellValueFactory(
                new PropertyValueFactory<Person, String>("email"));
            emailCol.setCellFactory(TextFieldTableCell.forTableColumn());
            emailCol.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Person, String>>()
                {
                    @Override
                    public void handle(TableColumn.CellEditEvent<Person, String> t)
                    {
                        t.getTableView().getItems().get(
                            t.getTablePosition().getRow()).setEmail(t.getNewValue());
                    }
                }
            );

            table.setItems(data);
            table.getColumns().addAll(firstNameCol, lastNameCol, emailCol);

            final TextField addFirstName = new TextField();
            addFirstName.setPromptText("First Name");
            addFirstName.setMaxWidth(firstNameCol.getPrefWidth());
            final TextField addLastName = new TextField();
            addLastName.setMaxWidth(lastNameCol.getPrefWidth());
            addLastName.setPromptText("Last Name");
            final TextField addEmail = new TextField();
            addEmail.setMaxWidth(emailCol.getPrefWidth());
            addEmail.setPromptText("Email");

            final Button addButton = new Button("Add");
            addButton.setOnAction(new EventHandler<ActionEvent>()
            {
                @Override
                public void handle(ActionEvent e)
                {
                    data.add(new Person(
                        addFirstName.getText(),
                        addLastName.getText(),
                        addEmail.getText()));
                    addFirstName.clear();
                    addLastName.clear();
                    addEmail.clear();
                }
            });

            hb.getChildren().addAll(addFirstName, addLastName, addEmail, addButton);
            hb.setSpacing(3);

            final VBox vbox = new VBox();
            vbox.setSpacing(5);
            vbox.setPadding(new Insets(10, 0, 0, 10));
            vbox.getChildren().addAll(label, table, hb);

            ((Group) scene.getRoot()).getChildren().addAll(vbox);

            stage.setScene(scene);
            stage.show();
        }

        public static class Person
        {

            private final SimpleStringProperty firstName;
            private final SimpleStringProperty lastName;
            private final SimpleStringProperty email;

            private Person(String fName, String lName, String email)
            {
                this.firstName = new SimpleStringProperty(fName);
                this.lastName = new SimpleStringProperty(lName);
                this.email = new SimpleStringProperty(email);
            }

            public String getFirstName()
            {
                return firstName.get();
            }

            public void setFirstName(String fName)
            {
                firstName.set(fName);
            }

            public String getLastName()
            {
                return lastName.get();
            }

            public void setLastName(String fName)
            {
                lastName.set(fName);
            }

            public String getEmail()
            {
                return email.get();
            }

            public void setEmail(String fName)
            {
                email.set(fName);
            }
        }
    }
}
