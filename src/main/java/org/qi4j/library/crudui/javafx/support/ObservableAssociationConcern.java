package org.qi4j.library.crudui.javafx.support;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import org.apache.polygene.api.association.Association;
import org.apache.polygene.api.common.AppliesTo;
import org.apache.polygene.api.common.AppliesToFilter;
import org.apache.polygene.api.concern.ConcernOf;

@AppliesTo({ObservableAssociationConcern.AssociationFilter.class})
public class ObservableAssociationConcern extends ConcernOf<InvocationHandler>
    implements InvocationHandler
{
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
    {
        // Will only come here if it is a Property<?> method.
        Association<?> association = (Association<?>) next.invoke(proxy, method, args);
        association = new ObservableAssociationWrapper<>(association);
        return association;
    }

    static class AssociationFilter
        implements AppliesToFilter
    {
        @Override
        public boolean appliesTo( Method method, Class<?> mixin, Class<?> compositeType, Class<?> modifierClass )
        {
            return Association.class.isAssignableFrom( method.getReturnType() );
        }
    }
}
