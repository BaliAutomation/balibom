package ac.bali.bom.ui;

import org.apache.polygene.api.entity.EntityReference;
import org.apache.polygene.api.injection.scope.Service;
import org.apache.polygene.api.injection.scope.Uses;
import org.apache.polygene.api.property.PropertyDescriptor;

public class EntityReferencePropertyControl extends PropertyControl<EntityReference>
{
    public EntityReferencePropertyControl(@Service PropertyCtrlFactory factory, @Uses PropertyDescriptor descriptor)
    {
        super(factory, false, "fix this");
    }

    @Override
    public void clear()
    {

    }

    @Override
    protected void updateTo(EntityReference value)
    {

    }

    @Override
    protected EntityReference currentValue()
    {
        return null;
    }
}
