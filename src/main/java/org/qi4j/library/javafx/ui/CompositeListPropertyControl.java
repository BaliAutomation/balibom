package org.qi4j.library.javafx.ui;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import org.apache.polygene.api.injection.scope.Service;
import org.apache.polygene.api.injection.scope.Structure;
import org.apache.polygene.api.injection.scope.Uses;
import org.apache.polygene.api.object.ObjectFactory;
import org.apache.polygene.api.structure.Module;
import org.apache.polygene.api.value.ValueComposite;
import org.apache.polygene.api.value.ValueDescriptor;
import org.apache.polygene.spi.PolygeneSPI;
import org.qi4j.library.javafx.support.Ignore;
import org.qi4j.library.javafx.support.MemberOrderComparator;

public class CompositeListPropertyControl<T> extends PropertyControl<List<T>>
{
    private final TableView<T> tableView;
    private final SimpleObjectProperty<List<T>> uiProperty = new SimpleObjectProperty<>();

    @Structure
    private ObjectFactory obf;

    @Structure
    PolygeneSPI spi;

    @SuppressWarnings("unchecked")
    public CompositeListPropertyControl(
        @Structure Module module,
        @Service PropertyCtrlFactory factory,
        @Uses ValueDescriptor descriptor)
    {
        super(factory, factory.nameOf(descriptor));

        // TableView
        tableView = new TableView<>();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
//        tableView.setStyle("-fx-border-style: solid; -fx-border-color: blue; -fx-border-width: 2px");

        uiProperty.addListener((observable, oldValue, newValue) -> {
            tableView.setItems(FXCollections.observableList(newValue));
        });

        ScrollPane scroll = new ScrollPane(tableView);
        scroll.setFitToHeight(true);
        scroll.setFitToWidth(true);
//        scroll.setStyle("-fx-border-style: solid; -fx-border-color: red; -fx-border-width: 2px");

        descriptor.state().properties()
            .filter(property -> property.metaInfo(Ignore.class) == null)
            .sorted(new MemberOrderComparator())
            .forEach(p ->
            {
                TableColumn<T, String> column = new TableColumn<>(factory.nameOf(p));
                column.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<T, String>, ObservableValue<String>>()
                {
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<T, String> pr)
                    {
                        T composite = pr.getValue();
                        Object value = spi.stateOf((ValueComposite) composite).propertyFor(p.accessor()).get();
                        if( value instanceof Map)
                        {
                            Map<Object,Object> m = (Map<Object, Object>) value;
                            value = m.entrySet().stream()
                                .filter(o -> o.getValue() != null && String.valueOf(o.getValue()).length() > 0 )
                                .map(o -> o.getKey() + "=" + o.getValue())
                                .collect(Collectors.joining("\n"));
                        }
                        return new SimpleStringProperty(String.valueOf(value));
                    }
                });
                tableView.getColumns().add(column);
            });
        VBox vbox = new VBox(new Label(factory.nameOf(descriptor)), scroll);
        vbox.setPadding(PADDING);
        getChildren().add(vbox);
    }

    @Override
    public void clear()
    {
        super.clear();
        tableView.getItems().clear();
    }

    @Override
    public Property<List<T>> uiProperty()
    {
        return uiProperty;
    }
}
