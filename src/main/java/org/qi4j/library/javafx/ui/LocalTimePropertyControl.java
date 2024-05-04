package org.qi4j.library.javafx.ui;

import java.time.LocalTime;
import org.apache.polygene.api.injection.scope.Service;
import org.apache.polygene.api.injection.scope.Uses;
import org.apache.polygene.api.property.PropertyDescriptor;

public class LocalTimePropertyControl extends DateTimePropertyControl<LocalTime>
{
    public LocalTimePropertyControl(@Service PropertyCtrlFactory factory, @Uses PropertyDescriptor descriptor)
    {
        super(factory, descriptor);
    }
}
