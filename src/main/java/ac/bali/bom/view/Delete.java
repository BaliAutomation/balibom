package ac.bali.bom.view;

import org.apache.polygene.api.injection.scope.Structure;
import org.apache.polygene.api.mixin.Mixins;
import org.apache.polygene.api.unitofwork.UnitOfWorkFactory;
import org.apache.polygene.api.unitofwork.concern.UnitOfWorkPropagation;
import org.qi4j.library.javafx.support.Action;
import org.qi4j.library.javafx.support.ActionScope;

import static org.apache.polygene.api.unitofwork.concern.UnitOfWorkPropagation.Propagation.MANDATORY;

@Mixins(Delete.Mixin.class)
public interface Delete<T>
{
    @UnitOfWorkPropagation(value = MANDATORY, usecase = "Delete")
    @Action(label="Delete", scope = ActionScope.composite)
    void delete( T item) throws Exception;

    class Mixin implements Delete {
        @Structure
        UnitOfWorkFactory uowf;

        @Override
        public void delete(Object item) throws Exception
        {
            uowf.currentUnitOfWork().remove(item);
        }
    }
}
