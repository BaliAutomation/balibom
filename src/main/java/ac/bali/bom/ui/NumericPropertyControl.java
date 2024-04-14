package ac.bali.bom.ui;

import org.apache.polygene.api.injection.scope.Service;
import org.apache.polygene.api.injection.scope.Uses;
import org.apache.polygene.api.property.Immutable;
import org.apache.polygene.api.property.PropertyDescriptor;

public abstract class NumericPropertyControl<T extends Number> extends PropertyControl<T>
{
    public NumericPropertyControl(@Service PropertyCtrlFactory factory, @Uses PropertyDescriptor descriptor)
    {
        super(factory,  descriptor.metaInfo(Immutable.class) != null, factory.nameOf(descriptor));
    }
}
