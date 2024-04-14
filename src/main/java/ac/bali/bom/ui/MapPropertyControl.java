package ac.bali.bom.ui;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import org.apache.polygene.api.injection.scope.Service;
import org.apache.polygene.api.injection.scope.Structure;
import org.apache.polygene.api.injection.scope.Uses;
import org.apache.polygene.api.property.PropertyDescriptor;
import org.apache.polygene.api.structure.Module;
import org.apache.polygene.api.value.ValueDescriptor;
import org.apache.polygene.spi.PolygeneSPI;

public class MapPropertyControl<K, V> extends PropertyControl<Map<K, V>>
{

    private final Label label;
    private final TableView<Map.Entry<K, V>> tableView;

    public MapPropertyControl(
        @Structure Module module,
        @Structure PolygeneSPI spi,
        @Service PropertyCtrlFactory factory,
        @Uses PropertyDescriptor descriptor)
    {
        super(factory, false, factory.nameOf(descriptor));

        label = new Label(factory.nameOf(descriptor));

        // TableView
        tableView = new TableView<>();
//        tableView.setStyle("-fx-border-style: solid; -fx-border-color: blue; -fx-border-width: 2px");

        ScrollPane scroll = new ScrollPane(tableView);
        scroll.setFitToHeight(true);
        scroll.setFitToWidth(true);
//        scroll.setStyle("-fx-border-style: solid; -fx-border-color: red; -fx-border-width: 2px");


        TableColumn<Map.Entry<K, V>, String> keyColumn = new TableColumn<>();
        keyColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getKey().toString()));

        Type type = descriptor.type();
        ObservableList<TableColumn<Map.Entry<K, V>, ?>> columns = tableView.getColumns();
        if (type instanceof Class)
        {
            columns.add(keyColumn);
            //noinspection unchecked
            Class<V> cls = (Class<V>) type;
            ValueDescriptor valueDescriptor = module.typeLookup().lookupValueModel(cls);
            valueDescriptor.state().properties().forEach(prop ->
                {
                    TableColumn<Map.Entry<K, V>, String> valueColumn = new TableColumn<>();
                    valueColumn.setCellValueFactory(param ->
                    {
                        // TODO
//                        spi.propertyDescriptorFor()
                        return new SimpleStringProperty(param.getValue().getValue().toString());
                    });

                    columns.add(valueColumn);
                }
            );
        } else
        {
            // toString() the value column
            TableColumn<Map.Entry<K, V>, String> valueColumn = new TableColumn<>();
            valueColumn.setCellFactory(column -> new ToStringTableCell<>());
            valueColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().toString()));
            columns.add(keyColumn);
            columns.add(valueColumn);
        }

        VBox vbox = new VBox(label, scroll);
        vbox.setPadding(PADDING);
        getChildren().add(vbox);
    }

    @Override
    public void clear()
    {
        tableView.getItems().clear();
    }

    @Override
    protected void updateTo(Map<K, V> value)
    {
        ObservableList<Map.Entry<K, V>> data = FXCollections.observableArrayList(value.entrySet());
        tableView.setItems(data);
    }

    @Override
    protected Map<K, V> currentValue()
    {
        Map<K, V> result = new HashMap<>();
        tableView.getItems().forEach(entry -> result.put(entry.getKey(), entry.getValue()));
        return result;
    }

    public static class ToStringTableCell<S, T> extends javafx.scene.control.TableCell<S, T>
    {
        @Override
        protected void updateItem(T item, boolean empty)
        {
            super.updateItem(item, empty);
            if (empty || item == null)
            {
                setText(null);
            } else
            {
                // Implement your rendering algorithm here
                setText(item.toString()); // Example: Just displaying the string value
            }
        }
    }
}
