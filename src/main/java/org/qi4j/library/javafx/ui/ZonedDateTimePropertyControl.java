package org.qi4j.library.javafx.ui;

import java.time.ZonedDateTime;
import org.apache.polygene.api.injection.scope.Service;
import org.apache.polygene.api.injection.scope.Uses;
import org.apache.polygene.api.property.PropertyDescriptor;

public class ZonedDateTimePropertyControl extends DateTimePropertyControl<ZonedDateTime>
{
    public ZonedDateTimePropertyControl(@Service PropertyCtrlFactory factory, @Uses PropertyDescriptor descriptor)
    {
        super(factory, descriptor);
    }

    @Override
    public void clear()
    {

    }

    @Override
    protected void updateTo(ZonedDateTime value)
    {

    }

    @Override
    protected ZonedDateTime currentValue()
    {
        return null;
    }
}