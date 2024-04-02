package ac.bali.bom.ui;

import org.apache.polygene.api.injection.scope.Service;
import org.apache.polygene.api.injection.scope.Uses;
import org.apache.polygene.api.property.PropertyDescriptor;

public class FloatPropertyControl extends NumericPropertyControl<Float>
{
    public FloatPropertyControl(@Service PropertyCtrlFactory factory, @Uses PropertyDescriptor descriptor)
    {
        super(factory,descriptor);
    }

    @Override
    public void clear()
    {

    }

    @Override
    protected void updateTo(Float value)
    {

    }

    @Override
    protected Float currentValue()
    {
        return null;
    }
}
