package ac.bali.bom.products;

import java.io.File;
import org.apache.polygene.api.concern.Concerns;
import org.apache.polygene.api.mixin.Mixins;
import org.apache.polygene.api.unitofwork.concern.UnitOfWorkConcern;
import org.apache.polygene.api.unitofwork.concern.UnitOfWorkPropagation;
import org.qi4j.library.javafx.support.Action;

import static org.apache.polygene.api.unitofwork.concern.UnitOfWorkPropagation.Propagation.MANDATORY;

@SuppressWarnings("unused")
@Mixins({ImportBomMixin.class, CostEstimationMixin.class})
@Concerns(UnitOfWorkConcern.class)
public interface ProductsService
{
    @UnitOfWorkPropagation(value= MANDATORY, usecase = "Import BOM")
    @Action(label="Import BOM...")
    Product importBom(File bomFile) throws Exception;

    @UnitOfWorkPropagation(value= MANDATORY, usecase = "Cost Estimation")
    @Action(label="Estimate Cost...", showResult = true)
    CostReport estimateCost(Product product, Integer quantity);

    String parseNameFromFile(File bomFile);

    String parseRevisionFromFile(File bomFile);

}
