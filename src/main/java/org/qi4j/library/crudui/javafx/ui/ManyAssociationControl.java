package org.qi4j.library.crudui.javafx.ui;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javafx.beans.binding.Bindings;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import org.apache.polygene.api.association.AssociationDescriptor;
import org.apache.polygene.api.association.ManyAssociation;
import org.apache.polygene.api.entity.EntityComposite;
import org.apache.polygene.api.entity.EntityDescriptor;
import org.apache.polygene.api.injection.scope.Service;
import org.apache.polygene.api.injection.scope.Structure;
import org.apache.polygene.api.injection.scope.Uses;
import org.apache.polygene.api.object.ObjectFactory;
import org.apache.polygene.api.structure.Module;
import org.apache.polygene.api.unitofwork.UnitOfWorkFactory;
import org.apache.polygene.spi.PolygeneSPI;
import org.qi4j.library.crudui.Ignore;
import org.qi4j.library.crudui.MemberOrderComparator;
import org.qi4j.library.crudui.javafx.support.ObservableManyAssociationWrapper;

public class ManyAssociationControl<T> extends PropertyControl<List<T>>
{
    private final TableView<T> tableView;
    private final SimpleObjectProperty<List<T>> uiProperty = new SimpleObjectProperty<>();
    private final Label label;
    private ManyAssociation<T> association;

    @Structure
    private ObjectFactory obf;

    @Structure
    UnitOfWorkFactory uowf;

    @Structure
    PolygeneSPI spi;

    @SuppressWarnings("unchecked")
    public ManyAssociationControl(
        @Structure Module module,
        @Service PropertyCtrlFactory factory,
        @Uses AssociationDescriptor assocDescr)
    {
        super(factory, factory.nameOf(assocDescr));
        label = labelOf();
        label.setAlignment(Pos.CENTER_LEFT);
        EntityDescriptor descriptor = module.typeLookup().lookupEntityModel((Class<?>) assocDescr.type());
        // TableView
        tableView = new TableView<>();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
//        tableView.setStyle("-fx-border-style: solid; -fx-border-color: blue; -fx-border-width: 2px");

        uiProperty.addListener((observable, oldValue, newValue) ->
        {
            label.setText(labelText + " (" + newValue.size() + ")");
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
                column.setCellValueFactory(pr ->
                {
                    T composite = pr.getValue();
                    Object value = spi.stateOf((EntityComposite) composite).propertyFor(p.accessor()).get();
                    if (value instanceof Map)
                    {
                        Map<Object, Object> m = (Map<Object, Object>) value;
                        value = m.entrySet().stream()
                            .filter(o -> o.getValue() != null && String.valueOf(o.getValue()).length() > 0)
                            .map(o -> o.getKey() + "=" + o.getValue())
                            .collect(Collectors.joining("\n"));
                    }
                    return new SimpleStringProperty(String.valueOf(value));
                });
                tableView.getColumns().add(column);
            });
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

    public void bind(ManyAssociation<T> p)
    {
        unbind();
        javafx.beans.property.Property<List<T>> value = uiProperty();
        if (value == null)
            return;
        if (p instanceof ObservableManyAssociationWrapper<T> assoc)
        {
            association = assoc;
            Bindings.bindBidirectional(value, assoc);
        }
    }

    private void unbind()
    {
        javafx.beans.property.Property<List<T>> value = uiProperty();
        if (value == null)
        {
            System.err.println("bind() not supported: " + this.getClass().getSimpleName());
            return;
        }
        if (association != null)
            Bindings.unbindBidirectional(value, association);
    }

}
