package org.qi4j.library.crudui.javafx.ui;

import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import org.apache.polygene.api.injection.scope.Service;
import org.apache.polygene.api.injection.scope.Uses;
import org.apache.polygene.api.property.PropertyDescriptor;
import org.qi4j.library.crudui.javafx.support.ObservablePropertyWrapper;

public class BooleanPropertyControl extends PropertyControl<Boolean>
{
    private final CheckBox field;
    private ObservablePropertyWrapper<Boolean> property;

    public BooleanPropertyControl(@Service PropertyCtrlFactory factory, @Uses PropertyDescriptor descriptor)
    {
        super(factory, factory.nameOf(descriptor));
        field = new CheckBox();
//        field.selectedProperty().addListener((observable, oldValue, newValue) ->
//            BooleanPropertyControl.this.fireEvent(new DirtyEvent(BooleanPropertyControl.this) ));
        field.setPadding(PADDING);
        Label label = labelOf();
        label.setPadding(PADDING);
        Pane box = wrapInHBox(label, field);
        HBox.setHgrow(field, Priority.ALWAYS);
        setAlignment(Pos.TOP_LEFT);
        setFillWidth(true);
        getChildren().add(box);
//        setStyle("-fx-border-style: solid; -fx-border-color: blue; -fx-border-width: 2px");

    }

    @Override
    public void clear()
    {
        super.clear();
        field.setSelected(false);
    }

    @Override
    public javafx.beans.property.Property<Boolean> uiProperty()
    {
        return field.selectedProperty();
    }
}
