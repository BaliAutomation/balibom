package org.qi4j.library.javafx.ui;

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

public abstract class NumericPropertyControl<T extends Number> extends PropertyControl<T>
{
    protected final TextInputControl field;

    @Structure
    private PolygeneAPI spi;

    public NumericPropertyControl(PropertyCtrlFactory factory, PropertyDescriptor descriptor, boolean withLabel)
    {
        super(factory, factory.nameOf(descriptor));

        field = new TextField();
//        field.textProperty().addListener((observable, oldValue, newValue) ->
//            NumericPropertyControl.this.fireEvent(new DirtyEvent(NumericPropertyControl.this) ));
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

    @Override
    public void clear()
    {
        super.clear();
        field.setText("");
    }

    private class NumberPropertyToStringConverter extends StringConverter<Property<T>>
    {
        @Structure
        PolygeneSPI spi;

        private final Property<T> value;
        private final ValueType type;

        public NumberPropertyToStringConverter(Property<T> value)
        {
            this.value = value;
            type = spi.propertyDescriptorFor(value).valueType();
        }

        @Override
        public String toString(Property<T> object)
        {
            if (object != value)
                throw new InternalError();
            return String.valueOf(object.get());
        }

        @SuppressWarnings("unchecked")
        @Override
        public Property<T> fromString(String string)
        {
            if (ValueType.LONG.equals(type))
            {
                T v = (T) Long.valueOf(string);
                value.set(v);
                return value;
            }
            if (ValueType.INTEGER.equals(type))
            {
                T v = (T) Integer.valueOf(string);
                value.set(v);
                return value;
            }
            if (ValueType.SHORT.equals(type))
            {
                T v = (T) Short.valueOf(string);
                value.set(v);
                return value;
            }
            if (ValueType.BYTE.equals(type))
            {
                T v = (T) Byte.valueOf(string);
                value.set(v);
                return value;
            }
            if (ValueType.FLOAT.equals(type))
            {
                T v = (T) Float.valueOf(string);
                value.set(v);
                return value;
            }
            if (ValueType.DOUBLE.equals(type))
            {
                T v = (T) Double.valueOf(string);
                value.set(v);
                return value;
            }
            throw new IllegalStateException("Unexpected value: " + type);
        }
    }
}
