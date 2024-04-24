package org.qi4j.library.javafx.ui;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.LongStringConverter;
import org.apache.polygene.api.injection.scope.Service;
import org.apache.polygene.api.injection.scope.Uses;
import org.apache.polygene.api.property.PropertyDescriptor;

public class DoublePropertyControl extends NumericPropertyControl<Double>
{
    public DoublePropertyControl(@Service PropertyCtrlFactory factory, @Uses PropertyDescriptor descriptor, @Uses boolean withLabel)
    {
        super(factory, descriptor, new TextFormatter<>(new DoubleStringConverter()));
    }

    @Override
    protected void updateTo(Double value)
    {
        field.setText(String.valueOf(value));
    }

    protected Double currentValue()
    {
        return Double.parseDouble(field.getText() );
    }

    @Override
    public void clear()
    {
        field.setText("");
    }
}
