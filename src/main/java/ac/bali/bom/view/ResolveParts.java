package ac.bali.bom.view;

import ac.bali.bom.parts.PartsService;
import ac.bali.bom.products.Bom;
import ac.bali.bom.products.Product;
import ac.bali.bom.ui.support.Action;
import ac.bali.bom.ui.support.ActionScope;
import org.apache.polygene.api.concern.Concerns;
import org.apache.polygene.api.injection.scope.Service;
import org.apache.polygene.api.mixin.Mixins;
import org.apache.polygene.api.unitofwork.concern.UnitOfWorkConcern;
import org.apache.polygene.api.unitofwork.concern.UnitOfWorkPropagation;

@Mixins(ResolveParts.Mixin.class)
@Concerns(UnitOfWorkConcern.class)
public interface ResolveParts
{
    @UnitOfWorkPropagation(usecase = "Resolve Parts")
    @Action(label="Resolve Parts", scope = ActionScope.composite)
    void resolveParts(Product product) throws Exception;

    class Mixin implements ResolveParts
    {
        @Service
        PartsService partsService;

        @Override
        public void resolveParts(Product product) throws Exception
        {
            Bom bom = product.bom().get();
            bom.items().get().stream().sequential().forEach( item -> {
                partsService.resolve(item);
            });
        }
    }
}
