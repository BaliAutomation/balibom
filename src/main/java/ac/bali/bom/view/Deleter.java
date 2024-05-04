package ac.bali.bom.view;

import java.util.List;
import org.apache.polygene.api.injection.scope.Structure;
import org.apache.polygene.api.mixin.Mixins;
import org.apache.polygene.api.unitofwork.UnitOfWorkFactory;
import org.apache.polygene.api.unitofwork.concern.UnitOfWorkPropagation;
import org.qi4j.library.javafx.support.Action;
import org.qi4j.library.javafx.support.ActionScope;

import static org.apache.polygene.api.unitofwork.concern.UnitOfWorkPropagation.Propagation.MANDATORY;

@Mixins(Deleter.Mixin.class)
public interface Deleter<T>
{
    @UnitOfWorkPropagation(value = MANDATORY, usecase = "Delete")
    @Action(label="Delete", scope = ActionScope.composite)
    void delete( List<T> item) throws Exception;

    class Mixin<T> implements Deleter<T>
    {
        @Structure
        UnitOfWorkFactory uowf;

        @Override
        public void delete(List<T> items) throws Exception
        {
            for( T item : items)
                uowf.currentUnitOfWork().remove(item);
        }
    }
}
