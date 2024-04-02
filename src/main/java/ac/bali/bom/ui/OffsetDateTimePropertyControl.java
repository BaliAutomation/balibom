package ac.bali.bom.ui;

import org.apache.polygene.api.injection.scope.Service;
import org.apache.polygene.api.injection.scope.Uses;
import org.apache.polygene.api.property.PropertyDescriptor;

import java.time.OffsetDateTime;

public class OffsetDateTimePropertyControl extends DateTimePropertyControl<OffsetDateTime>
{
    public OffsetDateTimePropertyControl(@Service PropertyCtrlFactory factory, @Uses PropertyDescriptor descriptor)
    {
        super(factory, descriptor);
    }

    @Override
    public void clear()
    {

    }

    @Override
    protected void updateTo(OffsetDateTime value)
    {

    }

    @Override
    protected OffsetDateTime currentValue()
    {
        return null;
    }
}
