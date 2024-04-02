package ac.bali.bom.ui;

import org.apache.polygene.api.injection.scope.Service;
import org.apache.polygene.api.injection.scope.Uses;
import org.apache.polygene.api.property.PropertyDescriptor;

import java.util.Map;

public class MapPropertyControl<K,V> extends PropertyControl<Map<K,V>>
{
    public MapPropertyControl(@Service PropertyCtrlFactory factory, @Uses PropertyDescriptor descriptor)
    {
        super(factory, false, "fix this");
    }

    @Override
    public void clear()
    {

    }

    @Override
    protected void updateTo(Map<K,V> value)
    {

    }

    @Override
    protected Map<K, V> currentValue()
    {
        return null;
    }
}
