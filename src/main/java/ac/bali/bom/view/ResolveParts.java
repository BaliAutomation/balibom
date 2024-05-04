package ac.bali.bom.view;

import ac.bali.bom.parts.PartsService;
import ac.bali.bom.products.Bom;
import ac.bali.bom.products.BomItem;
import ac.bali.bom.products.Product;
import org.apache.polygene.api.concern.Concerns;
import org.apache.polygene.api.injection.scope.Service;
import org.apache.polygene.api.injection.scope.Structure;
import org.apache.polygene.api.mixin.Mixins;
import org.apache.polygene.api.unitofwork.UnitOfWork;
import org.apache.polygene.api.unitofwork.UnitOfWorkFactory;
import org.apache.polygene.api.unitofwork.concern.UnitOfWorkConcern;
import org.apache.polygene.api.unitofwork.concern.UnitOfWorkPropagation;
import org.qi4j.library.javafx.support.Action;
import org.qi4j.library.javafx.support.ActionScope;

import static org.apache.polygene.api.unitofwork.concern.UnitOfWorkPropagation.Propagation.MANDATORY;

@Mixins(ResolveParts.Mixin.class)
@Concerns(UnitOfWorkConcern.class)
public interface ResolveParts
{
    @UnitOfWorkPropagation(value = MANDATORY, usecase = "Resolve Parts")
    @Action(label = "Resolve Parts", scope = ActionScope.composite)
    void resolveParts(Product product) throws Exception;

    class Mixin implements ResolveParts
    {
        @Service
        PartsService partsService;

        @Structure
        UnitOfWorkFactory uowf;

        @Override
        public void resolveParts(Product product) throws Exception
        {
            UnitOfWork uow = uowf.currentUnitOfWork();
            Bom bom = product.bom().get();
            bom.items()
                .references()
                .map(ref -> uow.get(BomItem.class, ref.identity()))
                .forEach(item -> partsService.resolve(item));
        }
    }
}
