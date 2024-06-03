package org.qi4j.library.crudui.javafx.ui;

import javafx.beans.property.Property;
import org.apache.polygene.api.injection.scope.Service;
import org.apache.polygene.api.injection.scope.Uses;
import org.apache.polygene.api.property.PropertyDescriptor;

public class ArrayPropertyControl extends PropertyControl<Object[]>
{
    public ArrayPropertyControl(@Service PropertyCtrlFactory factory, @Uses PropertyDescriptor descriptor)
    {
        super(factory, "fix this");
    }

    @Override
    public void clear()
    {
        super.clear();
    }

    @Override
    public Property<Object[]> uiProperty()
    {
        return null;
    }
}
