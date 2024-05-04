package org.qi4j.library.javafx.support;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import org.apache.polygene.api.association.ManyAssociation;
import org.apache.polygene.api.common.AppliesTo;
import org.apache.polygene.api.common.AppliesToFilter;
import org.apache.polygene.api.concern.ConcernOf;

@AppliesTo({ObservableManyAssociationConcern.ManyAssociationFilter.class})
public class ObservableManyAssociationConcern extends ConcernOf<InvocationHandler>
    implements InvocationHandler
{
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
    {
        // Will only come here if it is a Property<?> method.
        ManyAssociation<?> association = (ManyAssociation<?>) next.invoke(proxy, method, args);
        association = new ObservableManyAssociationWrapper<>(association);
        return association;
    }

    static class ManyAssociationFilter
        implements AppliesToFilter
    {
        @Override
        public boolean appliesTo( Method method, Class<?> mixin, Class<?> compositeType, Class<?> modifierClass )
        {
            return ManyAssociation.class.isAssignableFrom( method.getReturnType() );
        }
    }
}
