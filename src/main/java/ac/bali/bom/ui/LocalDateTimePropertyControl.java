package ac.bali.bom.ui;

import org.apache.polygene.api.injection.scope.Service;
import org.apache.polygene.api.injection.scope.Uses;
import org.apache.polygene.api.property.PropertyDescriptor;

import java.time.LocalDateTime;

public class LocalDateTimePropertyControl extends DateTimePropertyControl<LocalDateTime>
{
    public LocalDateTimePropertyControl(@Service PropertyCtrlFactory factory, @Uses PropertyDescriptor descriptor)
    {
        super(factory, descriptor);
    }

    @Override
    public void clear()
    {

    }

    @Override
    protected void updateTo(LocalDateTime value)
    {

    }

    @Override
    protected LocalDateTime currentValue()
    {
        return null;
    }
}
