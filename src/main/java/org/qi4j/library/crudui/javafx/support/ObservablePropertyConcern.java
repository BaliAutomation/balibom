package org.qi4j.library.crudui.javafx.support;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import org.apache.polygene.api.common.AppliesTo;
import org.apache.polygene.api.concern.ConcernOf;
import org.apache.polygene.api.property.Property;
import org.apache.polygene.api.property.PropertyMixin;

@AppliesTo({PropertyMixin.PropertyFilter.class})
public class ObservablePropertyConcern extends ConcernOf<InvocationHandler>
    implements InvocationHandler
{
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
    {
        // Will only come here if it is a Property<?> method.
        Property<?> property = (Property<?>) next.invoke(proxy, method, args);
        property = new ObservablePropertyWrapper<>(property);
        return property;
    }
}
