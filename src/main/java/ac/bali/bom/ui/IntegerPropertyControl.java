package ac.bali.bom.ui;

import org.apache.polygene.api.injection.scope.Service;
import org.apache.polygene.api.injection.scope.Uses;
import org.apache.polygene.api.property.PropertyDescriptor;

public class IntegerPropertyControl extends NumericPropertyControl<Integer>
{
    public IntegerPropertyControl(@Service PropertyCtrlFactory factory, @Uses PropertyDescriptor descriptor)
    {
        super(factory,descriptor);
    }

    @Override
    public void clear()
    {

    }

    @Override
    protected void updateTo(Integer value)
    {

    }

    @Override
    protected Integer currentValue()
    {
        return null;
    }
}
