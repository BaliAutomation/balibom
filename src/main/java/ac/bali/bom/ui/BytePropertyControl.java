package ac.bali.bom.ui;

import org.apache.polygene.api.injection.scope.Service;
import org.apache.polygene.api.injection.scope.Uses;
import org.apache.polygene.api.property.PropertyDescriptor;

public class BytePropertyControl extends NumericPropertyControl<Byte>
{
    public BytePropertyControl(@Service PropertyCtrlFactory factory, @Uses PropertyDescriptor descriptor)
    {
        super(factory,descriptor);
    }

    @Override
    public void clear()
    {

    }

    @Override
    protected void updateTo(Byte value)
    {

    }

    @Override
    protected Byte currentValue()
    {
        return null;
    }
}
