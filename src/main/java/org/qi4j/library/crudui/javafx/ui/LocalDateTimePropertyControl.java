package org.qi4j.library.crudui.javafx.ui;

import java.time.LocalDateTime;
import org.apache.polygene.api.injection.scope.Service;
import org.apache.polygene.api.injection.scope.Uses;
import org.apache.polygene.api.property.PropertyDescriptor;

public class LocalDateTimePropertyControl extends DateTimePropertyControl<LocalDateTime>
{
    public LocalDateTimePropertyControl(@Service PropertyCtrlFactory factory, @Uses PropertyDescriptor descriptor)
    {
        super(factory, factory.nameOf(descriptor));
    }
}
