package ac.bali.bom.ui;

import org.apache.polygene.api.identity.Identity;
import org.apache.polygene.api.injection.scope.Service;
import org.apache.polygene.api.injection.scope.Uses;
import org.apache.polygene.api.property.PropertyDescriptor;

public class IdentityPropertyControl extends PropertyControl<Identity>
{
    public IdentityPropertyControl(@Service PropertyCtrlFactory factory, @Uses PropertyDescriptor descriptor)
    {
        super(factory, false, "fix this");
    }

    @Override
    public void clear()
    {

    }

    @Override
    protected void updateTo(Identity value)
    {

    }

    @Override
    protected Identity currentValue()
    {
        return null;
    }
}
