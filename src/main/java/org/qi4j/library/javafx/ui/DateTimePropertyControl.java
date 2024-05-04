package org.qi4j.library.javafx.ui;

import java.time.temporal.Temporal;
import org.apache.polygene.api.injection.scope.Service;
import org.apache.polygene.api.injection.scope.Uses;
import org.apache.polygene.api.property.PropertyDescriptor;

public abstract class DateTimePropertyControl<T extends Temporal> extends PropertyControl<T>
{
    public DateTimePropertyControl(@Service PropertyCtrlFactory factory, @Uses PropertyDescriptor descriptor)
    {
        super(factory, "fix this");
    }


    @Override
    public void clear()
    {
        super.clear();

    }
}
