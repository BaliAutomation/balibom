package ac.bali.bom.ui;

import org.apache.polygene.api.injection.scope.Service;
import org.apache.polygene.api.injection.scope.Uses;
import org.apache.polygene.api.property.PropertyDescriptor;

public class DoublePropertyControl extends NumericPropertyControl<Double>
{
    public DoublePropertyControl(@Service PropertyCtrlFactory factory, @Uses PropertyDescriptor descriptor)
    {
        super(factory,descriptor);
    }

    @Override
    public void clear()
    {

    }

    @Override
    protected void updateTo(Double value)
    {

    }

    @Override
    protected Double currentValue()
    {
        return null;
    }
}
