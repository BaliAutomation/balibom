package org.qi4j.library.javafx.ui;

import java.time.LocalDate;
import org.apache.polygene.api.injection.scope.Service;
import org.apache.polygene.api.injection.scope.Uses;
import org.apache.polygene.api.property.PropertyDescriptor;

public class LocalDatePropertyControl extends DateTimePropertyControl<LocalDate>
{
    public LocalDatePropertyControl(@Service PropertyCtrlFactory factory, @Uses PropertyDescriptor descriptor)
    {
        super(factory, descriptor);
    }

    @Override
    public void clear()
    {

    }

    @Override
    protected void updateTo(LocalDate value)
    {

    }

    @Override
    protected LocalDate currentValue()
    {
        return null;
    }
}
