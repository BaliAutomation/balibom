package org.qi4j.library.javafx.ui;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.util.StringConverter;
import org.apache.polygene.api.PolygeneAPI;
import org.apache.polygene.api.injection.scope.Structure;
import org.apache.polygene.api.property.Property;
import org.apache.polygene.api.property.PropertyDescriptor;
import org.apache.polygene.api.type.ValueType;
import org.apache.polygene.spi.PolygeneSPI;
import org.qi4j.library.javafx.support.ObservablePropertyWrapper;

public abstract class NumericPropertyControl<T extends Number> extends PropertyControl<T>
{
    protected final TextInputControl field;

    @Structure
    private PolygeneAPI spi;
    private final javafx.beans.property.Property<T> uiProperty = new SimpleObjectProperty<>();

    public NumericPropertyControl(PropertyCtrlFactory factory, PropertyDescriptor descriptor, boolean withLabel)
    {
        super(factory, factory.nameOf(descriptor));

        field = new TextField();
        Pane box;
        if (withLabel)
        {
            Label label = labelOf();
            box = wrapInHBox(label, field);
        } else
        {
            box = wrapInHBox(field);
        }
        uiProperty.addListener((observable, oldValue, newValue) ->
        {
            field.setText(newValue.toString());
        });

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
        field.setText("");
    }

    @Override
    public javafx.beans.property.Property<T> uiProperty()
    {
        return uiProperty;
    }
}
