package ac.bali.bom.products;

import ac.bali.bom.parts.Part;
import ac.bali.bom.parts.PartsService;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.apache.polygene.api.concern.Concerns;
import org.apache.polygene.api.injection.scope.Service;
import org.apache.polygene.api.injection.scope.Structure;
import org.apache.polygene.api.mixin.Mixins;
import org.apache.polygene.api.unitofwork.UnitOfWork;
import org.apache.polygene.api.unitofwork.UnitOfWorkFactory;
import org.apache.polygene.api.unitofwork.concern.UnitOfWorkConcern;
import org.apache.polygene.api.unitofwork.concern.UnitOfWorkPropagation;
import org.apache.polygene.api.value.ValueBuilder;
import org.apache.polygene.api.value.ValueBuilderFactory;
import org.qi4j.library.crudui.Action;
import org.qi4j.library.crudui.ActionScope;

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
        ValueBuilderFactory vbf;

        @Structure
        UnitOfWorkFactory uowf;

        @Override
        public void resolveParts(Product product) throws Exception
        {
            List<String> resolveErrors = new ArrayList<>();
            UnitOfWork uow = uowf.currentUnitOfWork();
            Bom bom = product.bom().get();
            List<PartQuantity> parts = bom.items()
                .references()
                .map(ref -> uow.get(BomItem.class, ref.identity()))
                .filter( item ->
                {
                    String donNotPopulate = item.attributes().get().get("DNP");
                    return donNotPopulate == null || "".equals(donNotPopulate);
                })
                .map(item ->
                {
                    List<String> errors = new ArrayList<>();
                    Part part = partsService.resolve(item.mf().get(), item.mpn().get(), item.attributes().get(), errors);
                    if (part == null)
                    {
                        resolveErrors.add("ERROR: Unable to RESOLVE "  + item.designator().get() + " => " + item.mf().get() + " : " + item.mpn().get());
                        return null;
                    }
                    item.errors().set(errors);
                    Integer quantity = item.quantity().get();
                    ValueBuilder<PartQuantity> builder = vbf.newValueBuilder(PartQuantity.class);
                    PartQuantity prototype = builder.prototype();
                    prototype.part().set(part);
                    prototype.quantity().set(quantity);
                    return builder.newInstance();
                })
                .filter(Objects::nonNull)
                .toList();
            product.parts().set(parts);
            product.resolveErrors().set(resolveErrors);
        }
    }
}
