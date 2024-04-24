package org.qi4j.library.javafx.ui;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import org.apache.polygene.api.injection.scope.Service;
import org.apache.polygene.api.injection.scope.Uses;
import org.apache.polygene.api.property.Immutable;
import org.apache.polygene.api.property.PropertyDescriptor;

public abstract class NumericPropertyControl<T extends Number> extends PropertyControl<T>
{
    protected final TextInputControl field;

    public NumericPropertyControl(PropertyCtrlFactory factory, PropertyDescriptor descriptor, TextFormatter<T> formatter)
    {
        this( factory, descriptor, formatter, true);
    }

    public NumericPropertyControl(PropertyCtrlFactory factory, PropertyDescriptor descriptor, TextFormatter<T> formatter, boolean withLabel)
    {
        super(factory, descriptor.metaInfo(Immutable.class) != null, factory.nameOf(descriptor));

        field = new TextField();
        field.setTextFormatter(formatter);
        field.textProperty().addListener((observable, oldValue, newValue) ->
            NumericPropertyControl.this.fireEvent(new DirtyEvent(NumericPropertyControl.this) ));
        Pane box;
        if (withLabel)
        {
            Label label = labelOf();
            box = wrapInHBox(label, field);
        } else
        {
            box = wrapInHBox(field);
        }
        HBox.setHgrow(field, Priority.ALWAYS);
        setAlignment(Pos.TOP_LEFT);
        setFillWidth(true);
        getChildren().add(box);
//        setStyle("-fx-border-style: solid; -fx-border-color: blue; -fx-border-width: 2px");
    }
}
