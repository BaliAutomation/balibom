package ac.bali.bom.ui;

import org.apache.polygene.api.injection.scope.Service;
import org.apache.polygene.api.injection.scope.Uses;
import org.apache.polygene.api.property.PropertyDescriptor;

import java.math.BigInteger;

public class BigIntegerPropertyControl extends NumericPropertyControl<BigInteger>
{
    public BigIntegerPropertyControl(@Service PropertyCtrlFactory factory, @Uses PropertyDescriptor descriptor)
    {
        super(factory,descriptor);
    }

    @Override
    public void clear()
    {

    }

    @Override
    protected void updateTo(BigInteger value)
    {

    }

    @Override
    protected BigInteger currentValue()
    {
        return null;
    }
}
