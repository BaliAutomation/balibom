package org.qi4j.library.crudui.javafx.ui;

import java.time.temporal.Temporal;
import javafx.beans.property.Property;
import org.apache.polygene.api.property.PropertyDescriptor;

public abstract class DateTimePropertyControl<T extends Temporal> extends PropertyControl<T>
{
    public DateTimePropertyControl(PropertyCtrlFactory factory, PropertyDescriptor propDescr)
    {
        this(factory, factory.nameOf(propDescr));
    }

    public DateTimePropertyControl(PropertyCtrlFactory factory, String labelText)
    {
        super(factory, labelText);
    }

    @Override
    public void clear()
    {
        super.clear();
    }

    @Override
    public Property<T> uiProperty()
    {
        return null;
    }
}
