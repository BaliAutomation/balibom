package org.qi4j.library.crudui.javafx.ui;

import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.apache.polygene.api.injection.scope.Structure;
import org.apache.polygene.api.property.Property;
import org.apache.polygene.api.property.PropertyDescriptor;
import org.apache.polygene.spi.PolygeneSPI;
import org.qi4j.library.crudui.javafx.support.DataChangedEvent;
import org.qi4j.library.crudui.javafx.support.ObservablePropertyWrapper;

public abstract class PropertyControl<T> extends VBox
{
    static final Insets PADDING = new Insets(5, 10, 5, 10);

    @Structure
    protected PolygeneSPI spi;

    protected final String labelText;

    protected final PropertyCtrlFactory factory;
    private Property<T> property;
    private ObservablePropertyWrapper<String> observable;


    public PropertyControl(PropertyCtrlFactory factory, String labelText)
    {
        this.factory = factory;
        this.labelText = labelText;
    }

    public void clear() {
        unbind();
    }

    protected Label labelOf()
    {
        if (labelText == null)
            return null;
        Label label = new Label(labelText);
        label.setPrefWidth(150);
        label.setAlignment(Pos.CENTER_RIGHT);
        label.setPadding(PADDING);
        return label;
    }

    protected HBox wrapInHBox(Control... controls)
    {
        HBox box = new HBox(controls);
        box.setPadding(PADDING);
//        box.setDisable(immutable);
        return box;
    }

    protected VBox wrapInVBox(Control... controls)
    {
        VBox box = new VBox(controls);
        box.setPadding(PADDING);
//        box.setDisable(immutable);
        return box;
    }

    public T currentValue()
    {
        return property.get();
    }

    public abstract javafx.beans.property.Property<T> uiProperty();

    @SuppressWarnings({"rawtypes", "unchecked"})
    public void bind(Property<T> p)
    {
        unbind();
        javafx.beans.property.Property<T> uiProperty = uiProperty();
        if (uiProperty != null)
        {
            if (p instanceof ObservablePropertyWrapper prop)
            {
                property = prop;
                uiProperty.addListener((observable1, oldValue, newValue) -> fireEvent(new DataChangedEvent<>(observable1, oldValue, newValue)));
                PropertyDescriptor descriptor = spi.propertyDescriptorFor(p);
                if (!descriptor.isImmutable())
                {
                    Bindings.bindBidirectional(uiProperty, prop);
                } else
                {
                    uiProperty.bind(prop);
                }
            }
        }
    }

    private void unbind()
    {
        javafx.beans.property.Property<T> value = uiProperty();
        if( value == null)
        {
            System.err.println("bind() not supported: " + this.getClass().getSimpleName());
            return;
        }
        if( property != null )
            Bindings.unbindBidirectional(value, property);
    }
}
