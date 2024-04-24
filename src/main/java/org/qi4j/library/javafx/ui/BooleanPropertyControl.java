package org.qi4j.library.javafx.ui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import org.apache.polygene.api.injection.scope.Service;
import org.apache.polygene.api.injection.scope.Uses;
import org.apache.polygene.api.property.Immutable;
import org.apache.polygene.api.property.PropertyDescriptor;

public class BooleanPropertyControl extends PropertyControl<Boolean>
{
    private final CheckBox field;

    public BooleanPropertyControl(@Service PropertyCtrlFactory factory, @Uses PropertyDescriptor descriptor)
    {
        super(factory,  descriptor.metaInfo(Immutable.class) != null, factory.nameOf(descriptor));
        field = new CheckBox();
        field.selectedProperty().addListener((observable, oldValue, newValue) ->
            BooleanPropertyControl.this.fireEvent(new DirtyEvent(BooleanPropertyControl.this) ));
        Label label = labelOf();
        Pane box = wrapInHBox(label, field);
        HBox.setHgrow(field, Priority.ALWAYS);
        setAlignment(Pos.TOP_LEFT);
        setFillWidth(true);
        getChildren().add(box);
//        setStyle("-fx-border-style: solid; -fx-border-color: blue; -fx-border-width: 2px");

    }

    @Override
    protected void updateTo(Boolean value)
    {
        field.setSelected(value);
    }

    protected Boolean currentValue()
    {
        return field.selectedProperty().get();
    }

    @Override
    public void clear()
    {
        field.setSelected(false);
    }
}
