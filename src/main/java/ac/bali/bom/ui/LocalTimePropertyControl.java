package ac.bali.bom.ui;

import org.apache.polygene.api.injection.scope.Service;
import org.apache.polygene.api.injection.scope.Uses;
import org.apache.polygene.api.property.PropertyDescriptor;

import java.time.LocalTime;

public class LocalTimePropertyControl extends DateTimePropertyControl<LocalTime>
{
    public LocalTimePropertyControl(@Service PropertyCtrlFactory factory, @Uses PropertyDescriptor descriptor)
    {
        super(factory, descriptor);
    }

    @Override
    public void clear()
    {

    }

    @Override
    protected void updateTo(LocalTime value)
    {

    }

    @Override
    protected LocalTime currentValue()
    {
        return null;
    }
}
