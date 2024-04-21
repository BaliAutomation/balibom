package org.qi4j.library.javafx.ui;

import java.time.Duration;
import org.apache.polygene.api.injection.scope.Service;
import org.apache.polygene.api.injection.scope.Uses;
import org.apache.polygene.api.property.PropertyDescriptor;

public class DurationPropertyControl extends PropertyControl<Duration>
{
    public DurationPropertyControl(@Service PropertyCtrlFactory factory, @Uses PropertyDescriptor descriptor)
    {
        super(factory, false, "fix this");
    }

    @Override
    public void clear()
    {

    }

    @Override
    protected void updateTo(Duration value)
    {

    }

    @Override
    protected Duration currentValue()
    {
        return null;
    }
}
