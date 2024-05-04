package org.qi4j.library.javafx.support;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import org.apache.polygene.api.PolygeneAPI;
import org.apache.polygene.api.common.AppliesTo;
import org.apache.polygene.api.composite.Composite;
import org.apache.polygene.api.concern.ConcernOf;
import org.apache.polygene.api.injection.scope.Structure;
import org.apache.polygene.api.injection.scope.This;
import org.apache.polygene.api.property.Property;
import org.apache.polygene.api.property.PropertyDescriptor;
import org.apache.polygene.api.property.PropertyMixin;
import org.apache.polygene.spi.PolygeneSPI;

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
