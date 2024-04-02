package ac.bali.bom.ui;

import org.apache.polygene.api.injection.scope.Service;
import org.apache.polygene.api.injection.scope.Uses;
import org.apache.polygene.api.property.PropertyDescriptor;

import java.time.ZonedDateTime;

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
