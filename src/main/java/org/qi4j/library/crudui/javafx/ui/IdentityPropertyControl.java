package org.qi4j.library.crudui.javafx.ui;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import org.apache.polygene.api.identity.Identity;
import org.apache.polygene.api.injection.scope.Service;
import org.apache.polygene.api.injection.scope.Uses;
import org.apache.polygene.api.property.PropertyDescriptor;

public class IdentityPropertyControl extends PropertyControl<Identity>
{
    private final Label field;
    private final SimpleObjectProperty<Identity> uiProperty = new SimpleObjectProperty<>();

    public IdentityPropertyControl(@Service PropertyCtrlFactory factory, @Uses PropertyDescriptor descriptor)
    {
        super(factory, factory.nameOf(descriptor));
        field = new Label();
        field.setAlignment(Pos.CENTER_RIGHT);
        field.setPadding(PADDING);
        uiProperty.addListener((observable, oldValue, newValue) -> {
            String name = newValue.toString();
            field.setText(name);
        });
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
        super.clear();
        field.setText("");
    }

    @Override
    public Property<Identity> uiProperty()
    {
        return uiProperty;
    }
}
