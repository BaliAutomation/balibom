package org.qi4j.library.javafx.ui;

import javafx.beans.binding.Bindings;
import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import org.apache.polygene.api.injection.scope.Service;
import org.apache.polygene.api.injection.scope.Uses;
import org.apache.polygene.api.property.Property;
import org.apache.polygene.api.property.PropertyDescriptor;
import org.qi4j.library.javafx.support.ObservablePropertyWrapper;
import org.qi4j.library.javafx.support.RenderAsDescription;

public class StringPropertyControl extends PropertyControl<String>
{
    private final TextInputControl field;
    private ObservablePropertyWrapper<String> property;

    public StringPropertyControl(@Service PropertyCtrlFactory factory,
                                 @Uses PropertyDescriptor descriptor,
                                 @Uses Boolean withLabel)
    {
        super(factory, factory.nameOf(descriptor));
        if (descriptor.metaInfo(RenderAsDescription.class) == null)
        {
            field = new TextField();

        } else
        {
            TextArea ta = new TextArea();
            ta.setWrapText(true);
            field = ta;
        }
        if (descriptor.isImmutable())
        {
            field.setEditable(false);
        }
//        field.textProperty().addListener((observable, oldValue, newValue) -> StringPropertyControl.this.fireEvent(new DirtyEvent(StringPropertyControl.this)));
        Pane box;
        if (withLabel)
        {
            Label label = labelOf();
            if (field instanceof TextArea)
            {
                label.setAlignment(Pos.CENTER_LEFT);
                box = wrapInVBox(label, field);
            } else
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
    public void bind(Property<String> p)
    {
        StringProperty property1 = field.textProperty();
        if (property != null)
            Bindings.unbindBidirectional(property1, property);
        if (p instanceof ObservablePropertyWrapper<String> prop)
        {
            property = prop;
            Bindings.bindBidirectional(property1, prop);
        }
    }

    @Override
    public void clear()
    {
        super.clear();
        field.setText("");
    }

    @Override
    public javafx.beans.property.Property<String> uiProperty()
    {
        return field.textProperty();
    }
}
