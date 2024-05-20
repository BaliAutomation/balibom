package org.qi4j.library.javafx.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import org.apache.polygene.api.association.AssociationDescriptor;
import org.apache.polygene.api.injection.scope.Service;
import org.apache.polygene.api.injection.scope.Structure;
import org.apache.polygene.api.injection.scope.Uses;
import org.apache.polygene.api.object.ObjectFactory;
import org.apache.polygene.api.property.PropertyDescriptor;
import org.apache.polygene.api.structure.MetaInfoHolder;
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
        @Uses PropertyDescriptor propertyDescriptor,
        @Uses ValueDescriptor descriptor)
    {
        super(factory, factory.nameOf(propertyDescriptor));

        // TableView
        tableView = new TableView<>();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
//        tableView.setStyle("-fx-border-style: solid; -fx-border-color: blue; -fx-border-width: 2px");

        uiProperty.addListener((observable, oldValue, newValue) ->
        {
            tableView.setItems(FXCollections.observableList(newValue));
        });

        ScrollPane scroll = new ScrollPane(tableView);
        scroll.setFitToHeight(true);
        scroll.setFitToWidth(true);
//        scroll.setStyle("-fx-border-style: solid; -fx-border-color: red; -fx-border-width: 2px");

        List<MetaInfoHolder> members = new ArrayList<>();
        List<? extends MetaInfoHolder> properties = descriptor.state().properties().toList();
        List<? extends MetaInfoHolder> associations = descriptor.state().associations().toList();
        members.addAll(properties);
        members.addAll(associations);
        members.stream()
            .filter(property -> property.metaInfo(Ignore.class) == null)
            .sorted(new MemberOrderComparator())
            .forEach(member ->
            {
                Callback<TableColumn.CellDataFeatures<T, String>, ObservableValue<String>> callback = null;
                String columnName = null;
                switch (member.getClass().getSimpleName())
                {
                    case "PropertyModel" ->
                    {
                        PropertyDescriptor p = (PropertyDescriptor) member;
                        columnName = factory.nameOf(p);
                        callback = pr ->
                        {
                            T composite = pr.getValue();
                            Object value = spi.stateOf((ValueComposite) composite).propertyFor(p.accessor()).get();
                            if (value instanceof Map)
                            {
                                Map<Object, Object> m = (Map<Object, Object>) value;
                                value = m.entrySet().stream()
                                    .filter(o -> o.getValue() != null && String.valueOf(o.getValue()).length() > 0)
                                    .map(o -> o.getKey() + "=" + o.getValue())
                                    .collect(Collectors.joining("\n"));
                            }
                            return new SimpleStringProperty(String.valueOf(value));
                        };
                    }
                    case "AssociationModel" ->
                    {
                        AssociationDescriptor p = (AssociationDescriptor) member;
                        columnName = factory.nameOf(p);
                        callback = pr ->
                        {
                            T composite = pr.getValue();
                            Object value = spi.stateOf((ValueComposite) composite).associationFor(p.accessor()).get();
                            return new SimpleStringProperty(String.valueOf(value));
                        };
                    }
                }
                if( columnName != null )
                {
                    TableColumn<T, String> column = new TableColumn<>(columnName);
                    column.setCellValueFactory(callback);
                    tableView.getColumns().add(column);
                }
            });

        Label label = labelOf();
        label.setAlignment(Pos.CENTER_LEFT);
        VBox vbox = new VBox(label, scroll);
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
