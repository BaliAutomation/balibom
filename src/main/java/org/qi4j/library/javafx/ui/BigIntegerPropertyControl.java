package org.qi4j.library.javafx.ui;

import java.math.BigInteger;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.util.converter.BigIntegerStringConverter;
import org.apache.polygene.api.injection.scope.Service;
import org.apache.polygene.api.injection.scope.Uses;
import org.apache.polygene.api.property.PropertyDescriptor;

public class BigIntegerPropertyControl extends NumericPropertyControl<BigInteger>
{
    private final TextInputControl field;

    public BigIntegerPropertyControl(@Service PropertyCtrlFactory factory, @Uses PropertyDescriptor descriptor, @Uses boolean withLabel)
    {
        super(factory, descriptor);
        field = new TextField();
        field.setTextFormatter(new TextFormatter<>(new BigIntegerStringConverter()));
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
    protected void updateTo(BigInteger value)
    {
        field.setText(value.toString());
    }

    protected BigInteger currentValue()
    {
        return new BigInteger(field.getText() );
    }

    @Override
    public void clear()
    {
        field.setText("");
    }
}
