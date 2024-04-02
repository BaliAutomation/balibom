package ac.bali.bom.ui;

import org.apache.polygene.api.injection.scope.Service;
import org.apache.polygene.api.injection.scope.Uses;
import org.apache.polygene.api.property.PropertyDescriptor;

import java.time.Instant;

public class InstantPropertyControl extends DateTimePropertyControl<Instant>
{
    public InstantPropertyControl(@Service PropertyCtrlFactory factory, @Uses PropertyDescriptor descriptor)
    {
        super(factory, descriptor);
    }

    @Override
    public void clear()
    {

    }

    @Override
    protected void updateTo(Instant value)
    {

    }

    @Override
    protected Instant currentValue()
    {
        return null;
    }
}
