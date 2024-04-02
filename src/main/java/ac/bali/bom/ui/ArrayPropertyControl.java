package ac.bali.bom.ui;

import org.apache.polygene.api.injection.scope.Service;
import org.apache.polygene.api.injection.scope.Uses;
import org.apache.polygene.api.property.PropertyDescriptor;

public class ArrayPropertyControl extends PropertyControl<Object[]>
{
    public ArrayPropertyControl(@Service PropertyCtrlFactory factory, @Uses PropertyDescriptor descriptor)
    {
        super(factory, false, "fix this");
    }

    @Override
    public void clear()
    {

    }

    @Override
    protected void updateTo(Object[] value)
    {

    }

    @Override
    protected Object[] currentValue()
    {
        return new Object[0];
    }
}
