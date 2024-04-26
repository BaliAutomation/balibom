package org.qi4j.library.javafx.ui;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.util.StringConverter;
import org.apache.polygene.api.identity.Identity;
import org.apache.polygene.api.identity.StringIdentity;
import org.apache.polygene.api.injection.scope.Service;
import org.apache.polygene.api.injection.scope.Uses;
import org.apache.polygene.api.property.Immutable;
import org.apache.polygene.api.property.PropertyDescriptor;

public class IdentityPropertyControl extends PropertyControl<Identity>
{
    private final Label field;

    public IdentityPropertyControl(@Service PropertyCtrlFactory factory, @Uses PropertyDescriptor descriptor)
    {
        super(factory, descriptor.metaInfo(Immutable.class) != null, factory.nameOf(descriptor));
        field = new Label();
        field.setAlignment(Pos.CENTER_RIGHT);
        field.setPadding(PADDING);
        Pane box;
        Label label = labelOf();
        box = wrapInHBox(label, field);
        HBox.setHgrow(field, Priority.ALWAYS);
        setAlignment(Pos.TOP_LEFT);
        setFillWidth(true);
        getChildren().add(box);
//        setStyle("-fx-border-style: solid; -fx-border-color: blue; -fx-border-width: 2px");
    }

    @Override
    public void clear()
    {
        field.setText("");
    }

    @Override
    protected void updateTo(Identity value)
    {
        if( value == null )
            return;
        field.setText(value.toString());
    }

    @Override
    protected Identity currentValue()
    {
        String text = field.getText();
        return StringIdentity.identityOf(text);
    }
}
