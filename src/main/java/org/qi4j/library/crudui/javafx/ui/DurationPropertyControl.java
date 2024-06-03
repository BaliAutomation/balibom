package org.qi4j.library.crudui.javafx.ui;

import java.time.Duration;
import javafx.beans.property.Property;
import org.apache.polygene.api.injection.scope.Service;
import org.apache.polygene.api.injection.scope.Uses;
import org.apache.polygene.api.property.PropertyDescriptor;

public class DurationPropertyControl extends PropertyControl<Duration>
{
    public DurationPropertyControl(@Service PropertyCtrlFactory factory, @Uses PropertyDescriptor descriptor)
    {
        super(factory, "fix this");
    }

    @Override
    public void clear()
    {
        super.clear();
    }

    @Override
    public Property<Duration> uiProperty()
    {
        return null;
    }
}
