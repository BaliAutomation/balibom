package ac.bali.bom.ui;

import ac.bali.bom.support.ListRenderer;
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
import org.apache.polygene.api.common.Optional;
import org.apache.polygene.api.injection.scope.Service;
import org.apache.polygene.api.injection.scope.Structure;
import org.apache.polygene.api.injection.scope.Uses;
import org.apache.polygene.api.object.ObjectFactory;
import org.apache.polygene.api.property.PropertyDescriptor;

import java.util.List;
import java.util.function.Consumer;

public class ListPropertyControl<T> extends PropertyControl<List<T>>
{
    ListView<T> listView;

    @SuppressWarnings("unchecked")
    public ListPropertyControl(@Service PropertyCtrlFactory factory,
                               @Uses @Optional PropertyDescriptor descriptor,
                               @Structure ObjectFactory obf)
    {
        super(factory, false, null);
        listView = new ListView<>();
        listView.setPadding(PADDING);
        if( descriptor != null )
        {
            ListRenderer renderer = descriptor.metaInfo(ListRenderer.class);
            listView.setCellFactory(param -> (ListCell<T>) obf.newObject(renderer.value()));
        }
        else
        {
            listView.setCellFactory(param -> (ListCell<T>) obf.newObject(NameListCell.class));
        }
        ScrollPane scrollPane2 = new ScrollPane(listView);
        scrollPane2.setFitToWidth(true);
        scrollPane2.setFitToHeight(true);
        VBox scrollPane = new VBox(scrollPane2);
        HBox.setHgrow(scrollPane, Priority.ALWAYS);
        scrollPane.setFillWidth(true);
        VBox.setVgrow(scrollPane2,Priority.ALWAYS);
//        scrollPane2.setStyle("-fx-border-color: blue; ");
//        scrollPane.setStyle("-fx-border-color: purple; ");

        HBox box;
        if( descriptor == null )
        {
            box = new HBox(scrollPane);
        } else
        {
            String name = factory.nameOf(descriptor);
            Label label = new Label(name);
            label.setPrefWidth(150);
            label.setAlignment(Pos.CENTER_RIGHT);
            label.setPadding(PADDING);
            box = new HBox(label, scrollPane);
        }
//        box.setStyle("-fx-border-color: red;");
        box.setPadding(PADDING);
        box.setFillHeight(true);
        VBox.setVgrow(box, Priority.ALWAYS);
        getChildren().add(box);
        setFillWidth(true);
//        setStyle("-fx-border-color: green;");
    }

    @Override
    public void clear()
    {
        listView.getItems().clear();
    }

    public void addSelectionHandler(ChangeListener<T> listener)
    {
        listView.getSelectionModel().selectedItemProperty().addListener(listener);
    }

    public void removeEventHandler(ListChangeListener<T> listener)
    {
        listView.getItems().removeListener(listener);
    }

    public void selected(Consumer<T> selected)
    {
        listView.getSelectionModel().getSelectedItems().forEach(selected);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void updateTo(List<T> value)
    {
        ObservableList<T> list = listView.getItems();
        list.clear();
        ObservableList<T> items;
        if (value instanceof ObservableList<T>)
        {
            items = (ObservableList<T>) value;
        } else
        {
            items = FXCollections.observableArrayList(value);
        }
        listView.setItems(items);
    }

    @Override
    protected List<T> currentValue()
    {
        return listView.getItems();
    }
}
