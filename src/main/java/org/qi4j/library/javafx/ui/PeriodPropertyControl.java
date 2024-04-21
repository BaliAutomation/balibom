package org.qi4j.library.javafx.ui;

import java.time.Period;
import org.apache.polygene.api.injection.scope.Service;
import org.apache.polygene.api.injection.scope.Uses;
import org.apache.polygene.api.property.PropertyDescriptor;

public class PeriodPropertyControl extends PropertyControl<Period>
{
    public PeriodPropertyControl(@Service PropertyCtrlFactory factory, @Uses PropertyDescriptor descriptor)
    {
        super(factory, false, "fix this");
    }

    @Override
    public void clear()
    {

    }

    @Override
    protected void updateTo(Period value)
    {

    }

    @Override
    protected Period currentValue()
    {
        return null;
    }
}
