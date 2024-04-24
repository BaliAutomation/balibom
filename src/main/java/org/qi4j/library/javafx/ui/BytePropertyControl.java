package org.qi4j.library.javafx.ui;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.util.converter.ByteStringConverter;
import javafx.util.converter.LongStringConverter;
import org.apache.polygene.api.injection.scope.Service;
import org.apache.polygene.api.injection.scope.Uses;
import org.apache.polygene.api.property.PropertyDescriptor;

public class BytePropertyControl extends NumericPropertyControl<Byte>
{
    public BytePropertyControl(@Service PropertyCtrlFactory factory, @Uses PropertyDescriptor descriptor, @Uses boolean withLabel)
    {
        super(factory, descriptor, new TextFormatter<>(new ByteStringConverter()));
    }

    @Override
    protected void updateTo(Byte value)
    {
        field.setText(String.valueOf(value));
    }

    protected Byte currentValue()
    {
        return Byte.parseByte(field.getText() );
    }

    @Override
    public void clear()
    {
        field.setText("");
    }
}
