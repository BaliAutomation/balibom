package org.qi4j.library.crudui.javafx.ui;

import java.time.Instant;
import org.apache.polygene.api.injection.scope.Service;
import org.apache.polygene.api.injection.scope.Uses;
import org.apache.polygene.api.property.PropertyDescriptor;

public class InstantPropertyControl extends DateTimePropertyControl<Instant>
{
    public InstantPropertyControl(@Service PropertyCtrlFactory factory, @Uses PropertyDescriptor descriptor)
    {
        super(factory, factory.nameOf(descriptor));
    }

    @Override
    public void clear()
    {
        super.clear();
    }

}
