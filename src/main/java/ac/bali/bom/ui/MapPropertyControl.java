package ac.bali.bom.ui;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.apache.polygene.api.injection.scope.Service;
import org.apache.polygene.api.injection.scope.Uses;
import org.apache.polygene.api.property.PropertyDescriptor;

import java.util.Map;

public class MapPropertyControl<K,V> extends PropertyControl<Map<K,V>>
{

    private final TableView<Map.Entry<K, V>> tableView;

    public MapPropertyControl(@Service PropertyCtrlFactory factory, @Uses PropertyDescriptor descriptor)
    {
        super(factory, false, factory.nameOf(descriptor));

        // TableView
        tableView = new TableView<>();
        TableColumn<Map.Entry<K,V>, String> keyColumn = new TableColumn<>("Key");
        keyColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getKey().toString()));

        TableColumn<Map.Entry<K,V>, String> valueColumn = new TableColumn<>("Value");
        valueColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().toString()));

        tableView.getColumns().addAll(keyColumn, valueColumn);

        // Set cell factory for value column
        valueColumn.setCellFactory(column -> new ToStringTableCell<>());

    }

    @Override
    public void clear()
    {

    }

    @Override
    protected void updateTo(Map<K,V> value)
    {
        ObservableList<Map.Entry<K, V>> data = FXCollections.observableArrayList(value.entrySet());
        tableView.setItems(data);
    }

    @Override
    protected Map<K, V> currentValue()
    {
        return null;
    }

    public static class ToStringTableCell<S,T> extends javafx.scene.control.TableCell<S,T>
    {
        @Override
        protected void updateItem(T item, boolean empty)
        {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
            } else {
                // Implement your rendering algorithm here
                setText(item.toString()); // Example: Just displaying the string value
            }
        }
    }
}
