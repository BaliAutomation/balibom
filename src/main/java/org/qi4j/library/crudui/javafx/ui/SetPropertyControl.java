package org.qi4j.library.crudui.javafx.ui;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.apache.polygene.api.association.AssociationDescriptor;
import org.apache.polygene.api.common.Optional;
import org.apache.polygene.api.injection.scope.Service;
import org.apache.polygene.api.injection.scope.Structure;
import org.apache.polygene.api.injection.scope.Uses;
import org.apache.polygene.api.object.ObjectFactory;
import org.apache.polygene.api.property.PropertyDescriptor;
import org.apache.polygene.api.structure.MetaInfoHolder;
import org.qi4j.library.crudui.javafx.support.ListRenderer;
import org.qi4j.library.crudui.Height;

public class SetPropertyControl<T> extends PropertyControl<Set<T>>
{
    ListView<T> listView;
    SimpleObjectProperty<Set<T>> uiProperty = new SimpleObjectProperty<>();

    @SuppressWarnings("unchecked")
    public SetPropertyControl(@Service PropertyCtrlFactory factory,
                              @Uses @Optional MetaInfoHolder meta,
                              @Uses @Optional PropertyDescriptor propDescriptor,
                              @Uses @Optional AssociationDescriptor assocDescriptor,
                              @Structure ObjectFactory obf, @Uses @Optional Boolean withLabel)
    {
        super(factory, null);
        listView = new ListView<>();
        listView.setPadding(PADDING);
        uiProperty.addListener((observable, oldValue, newValue) -> {
            listView.setItems(FXCollections.observableList(newValue.stream().toList()));
        });
        ScrollPane scrollPane2 = new ScrollPane(listView);
        scrollPane2.setFitToWidth(true);
        scrollPane2.setFitToHeight(true);
        if (meta != null)
        {
            Height height = meta.metaInfo(Height.class);
            if (height != null)
            {
                if (height.pref() > 0)
                    scrollPane2.setPrefHeight(height.pref());
                if (height.max() > 0)
                    scrollPane2.setMaxHeight(height.max());
                if (height.min() > 0)
                    scrollPane2.setMinHeight(height.min());
            }
            ListRenderer renderer = meta.metaInfo(ListRenderer.class);
            Class<?> cls = renderer == null ? NameListCell.class : renderer.value();
            listView.setCellFactory(param -> (ListCell<T>) obf.newObject(cls));
        } else
        {
            listView.setCellFactory(param -> (ListCell<T>) obf.newObject(NameListCell.class));
        }
        VBox scrollPane = new VBox(scrollPane2);
        HBox.setHgrow(scrollPane, Priority.ALWAYS);
        scrollPane.setFillWidth(true);
        setVgrow(scrollPane2, Priority.ALWAYS);
//        scrollPane2.setStyle("-fx-border-color: blue; ");
//        scrollPane.setStyle("-fx-border-color: purple; ");

        HBox box;
        if ((propDescriptor == null && assocDescriptor == null) || !withLabel)
        {
            box = new HBox(scrollPane);
        } else
        {
            String name = factory.nameOf(propDescriptor, assocDescriptor);
            Label label = new Label(name);
            label.setPrefWidth(150);
            label.setAlignment(Pos.CENTER_RIGHT);
            label.setPadding(PADDING);
            box = new HBox(label, scrollPane);
        }
//        box.setStyle("-fx-border-color: red;");
        box.setPadding(PADDING);
        box.setFillHeight(true);
        setVgrow(box, Priority.ALWAYS);
        getChildren().add(box);
        setFillWidth(true);
//        setStyle("-fx-border-color: green;");
    }

    @Override
    public void clear()
    {
        super.clear();
        listView.setItems(FXCollections.emptyObservableList());
    }

    public void addSelectionHandler(ChangeListener<T> listener)
    {
        listView.getSelectionModel().selectedItemProperty().addListener(listener);
    }

    public void removeEventHandler(ListChangeListener<T> listener)
    {
        listView.getItems().removeListener(listener);
    }

    public List<T> selection()
    {
        return listView.getSelectionModel().getSelectedItems();
    }

    public void selected(Consumer<T> selected)
    {
        listView.getSelectionModel().getSelectedItems().forEach(selected);
    }

    public void setValue(ObservableList<T> items)
    {
        listView.setItems(items);
    }

    @Override
    public Property<Set<T>> uiProperty()
    {
        return uiProperty;
    }
}
