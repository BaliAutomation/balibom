package ac.bali.bom.ui;

import org.apache.polygene.api.injection.scope.Service;
import org.apache.polygene.api.injection.scope.Uses;
import org.apache.polygene.api.property.PropertyDescriptor;

import java.math.BigDecimal;

public class BigDecimalPropertyControl extends NumericPropertyControl<BigDecimal>
{
    public BigDecimalPropertyControl(@Service PropertyCtrlFactory factory, @Uses PropertyDescriptor descriptor)
    {
        super(factory,descriptor);

    }

    @Override
    public void clear()
    {

    }

    @Override
    protected void updateTo(BigDecimal value)
    {
        dirty();
    }

    @Override
    protected BigDecimal currentValue()
    {
        return null;
    }
}
