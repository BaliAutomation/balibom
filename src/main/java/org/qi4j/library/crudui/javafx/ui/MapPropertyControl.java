package org.qi4j.library.crudui.javafx.ui;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.stream.Collectors;
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
import org.apache.polygene.api.association.AssociationStateHolder;
import org.apache.polygene.api.composite.StatefulCompositeDescriptor;
import org.apache.polygene.api.entity.EntityComposite;
import org.apache.polygene.api.entity.EntityDescriptor;
import org.apache.polygene.api.entity.EntityReference;
import org.apache.polygene.api.injection.scope.Service;
import org.apache.polygene.api.injection.scope.Structure;
import org.apache.polygene.api.injection.scope.Uses;
import org.apache.polygene.api.object.ObjectFactory;
import org.apache.polygene.api.property.Property;
import org.apache.polygene.api.property.PropertyDescriptor;
import org.apache.polygene.api.structure.Module;
import org.apache.polygene.api.value.ValueComposite;
import org.apache.polygene.api.value.ValueDescriptor;
import org.apache.polygene.spi.PolygeneSPI;
import org.qi4j.library.crudui.MemberOrderComparator;
import org.qi4j.library.crudui.RenderAsValue;

public class MapPropertyControl<K, V> extends PropertyControl<Map<K, V>>
{
    private final TableView<Map.Entry<K, V>> tableView;
    private final SimpleObjectProperty<Map<K,V>> uiProperty = new SimpleObjectProperty<>();
    private final Label label;

    @Structure
    private ObjectFactory obf;

    @Structure
    PolygeneSPI spi;

    @SuppressWarnings("unchecked")
    public MapPropertyControl(
        @Structure Module module,
        @Service PropertyCtrlFactory factory,
        @Uses PropertyDescriptor descriptor)
    {
        super(factory, factory.nameOf(descriptor));
        label = new Label(labelText);

        // TableView
        tableView = new TableView<>();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
//        tableView.setStyle("-fx-border-style: solid; -fx-border-color: blue; -fx-border-width: 2px");
        uiProperty.addListener((observable, oldValue, newValue) ->
        {
            ObservableList<Map.Entry<K, V>> list = FXCollections.observableList(newValue.entrySet().stream().toList());
            if( label != null)
                label.setText(labelText + " (" + newValue.size() + ")");
            tableView.setItems(list);
        });
        ScrollPane scroll = new ScrollPane(tableView);
        scroll.setFitToHeight(true);
        scroll.setFitToWidth(true);
//        scroll.setStyle("-fx-border-style: solid; -fx-border-color: red; -fx-border-width: 2px");


        TableColumn<Map.Entry<K, V>, String> keyColumn = new TableColumn<>();
        keyColumn.setMaxWidth( 100);
        keyColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getKey().toString()));

        Type type = descriptor.type();
        ObservableList<TableColumn<Map.Entry<K, V>, ?>> columns = tableView.getColumns();
        if (type instanceof ParameterizedType parameterizedType)
        {
            columns.add(keyColumn);

            Type rawType = parameterizedType.getRawType();
            Type[] typeArguments = parameterizedType.getActualTypeArguments();
            if (rawType.equals(Map.class))
            {
                TableColumn<Map.Entry<K, V>, String> valueColumn = new TableColumn<>();
                valueColumn.setPrefWidth(USE_COMPUTED_SIZE);

                EntityDescriptor entityModel = module.typeLookup().lookupEntityModel((Class<?>) typeArguments[1]);
                ValueDescriptor valueModel = module.typeLookup().lookupValueModel((Class<?>) typeArguments[1]);
                valueColumn.setCellFactory(column -> new ToStringTableCell<>());

                if (valueModel != null || entityModel != null)
                    valueColumn.setCellValueFactory(column -> renderValueCell(column.getValue().getValue()));
                else
                    valueColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().toString()));
                columns.add(valueColumn);
            }
        } else if (type instanceof Class)
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
        super.clear();
        tableView.setItems(FXCollections.emptyObservableList());
    }

    @Override
    public javafx.beans.property.Property<Map<K, V>> uiProperty()
    {
        return uiProperty;
    }

    ObservableValue<String> renderValueCell(V composite)
    {
        if( composite == null )
            return null;
        String text;
        if (ValueComposite.class.isAssignableFrom(composite.getClass()))
        {
            StatefulCompositeDescriptor descriptor = spi.valueDescriptorFor(composite);
            text = descriptor.state()
                .properties()
                .filter(p -> p.metaInfo(RenderAsValue.class) != null)
                .sorted(new MemberOrderComparator())
                .map(p -> composeValue(spi.stateOf((ValueComposite) composite), p))
                .collect(Collectors.joining(", "));

        } else if (EntityComposite.class.isAssignableFrom(composite.getClass()))
        {
            StatefulCompositeDescriptor descriptor = spi.entityDescriptorFor(composite);
            text = descriptor.state()
                .properties()
                .filter(p -> p.metaInfo(RenderAsValue.class) != null)
                .sorted(new MemberOrderComparator())
                .map(p -> composeValue(spi.stateOf((EntityComposite) composite), p))
                .collect(Collectors.joining(", "));

        } else if (composite instanceof EntityReference)
        {
            text = ((EntityReference) composite).identity().toString();
        } else
        {
            throw new IllegalArgumentException("Only Stateful Composites can be used as values in Map.");
        }
        return new SimpleStringProperty(text);
    }

    private String composeValue(AssociationStateHolder state, PropertyDescriptor p)
    {
        Property<?> property = state.propertyFor(p.accessor());
        String title = spi.propertyDescriptorFor(property).metaInfo(RenderAsValue.class).title();
        if( title.length() == 0 )
            return property.get().toString();
        else
            return title + "=" + property.get().toString();
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
                setText(item.toString());
            }
        }
    }
}
