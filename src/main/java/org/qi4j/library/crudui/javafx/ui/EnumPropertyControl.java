package org.qi4j.library.crudui.javafx.ui;

import java.time.LocalDate;
import java.util.List;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import org.apache.polygene.api.injection.scope.Service;
import org.apache.polygene.api.injection.scope.Uses;
import org.apache.polygene.api.property.PropertyDescriptor;
import org.apache.polygene.api.type.EnumType;
import org.qi4j.library.crudui.RenderAsDescription;
import org.qi4j.library.crudui.SecretField;

public class EnumPropertyControl<T> extends PropertyControl<T>
{
    private final ChoiceBox<T> field;
    private final javafx.beans.property.Property<T> uiProperty = new SimpleObjectProperty<>();

    public EnumPropertyControl(@Service PropertyCtrlFactory factory, @Uses PropertyDescriptor descriptor)
    {
        super(factory, factory.nameOf(descriptor));
        EnumType type = (EnumType) descriptor.valueType();
        T[] enumConstants = (T[]) type.primaryType().getEnumConstants();
        field = createChoiceBox(enumConstants);
        Pane box = wrapInHBox(labelOf(), field);
        HBox.setHgrow(field, Priority.ALWAYS);
        setAlignment(Pos.TOP_LEFT);
        setFillWidth(true);
        getChildren().add(box);
        field.valueProperty().bindBidirectional(uiProperty);
//        setStyle("-fx-border-style: solid; -fx-border-color: blue; -fx-border-width: 2px");
    }

    private ChoiceBox<T> createChoiceBox(T[] enums)
    {
        ObservableList<T> data = FXCollections.observableArrayList(enums);
        ChoiceBox<T> comboBox = new ChoiceBox<>(data);
        comboBox.getSelectionModel().selectFirst();
        comboBox.setPadding(PropertyControl.PADDING);
        return comboBox;
    }

    @Override
    public void clear()
    {
        super.clear();
        field.getSelectionModel().clearSelection();
    }

    @Override
    public javafx.beans.property.Property<T> uiProperty()
    {
        return uiProperty;
    }
}
