package org.qi4j.library.javafx.support;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import org.apache.polygene.api.association.NamedAssociation;
import org.apache.polygene.api.common.AppliesTo;
import org.apache.polygene.api.common.AppliesToFilter;
import org.apache.polygene.api.concern.ConcernOf;

@AppliesTo({ObservableNamedAssociationConcern.NamedAssociationFilter.class})
public class ObservableNamedAssociationConcern extends ConcernOf<InvocationHandler>
    implements InvocationHandler
{
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
    {
        // Will only come here if it is a Property<?> method.
        NamedAssociation<?> association = (NamedAssociation<?>) next.invoke(proxy, method, args);
        association = new ObservableNamedAssociationWrapper<>(association);
        return association;
    }

    static class NamedAssociationFilter
        implements AppliesToFilter
    {
        @Override
        public boolean appliesTo( Method method, Class<?> mixin, Class<?> compositeType, Class<?> modifierClass )
        {
            return NamedAssociation.class.isAssignableFrom( method.getReturnType() );
        }
    }
}
