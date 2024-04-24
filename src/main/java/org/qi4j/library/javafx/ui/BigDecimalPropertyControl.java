package org.qi4j.library.javafx.ui;

import java.math.BigDecimal;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.util.converter.BigDecimalStringConverter;
import javafx.util.converter.LongStringConverter;
import org.apache.polygene.api.injection.scope.Service;
import org.apache.polygene.api.injection.scope.Uses;
import org.apache.polygene.api.property.PropertyDescriptor;

public class BigDecimalPropertyControl extends NumericPropertyControl<BigDecimal>
{
    public BigDecimalPropertyControl(@Service PropertyCtrlFactory factory, @Uses PropertyDescriptor descriptor, @Uses boolean withLabel)
    {
        super(factory, descriptor, new TextFormatter<>(new BigDecimalStringConverter()));
    }

    @Override
    protected void updateTo(BigDecimal value)
    {
        field.setText(value.toString());
    }

    protected BigDecimal currentValue()
    {
        return new BigDecimal(field.getText() );
    }

    @Override
    public void clear()
    {
        field.setText("");
    }
}
