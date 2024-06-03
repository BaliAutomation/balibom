package ac.bali.bom.products;

import ac.bali.bom.parts.Part;
import ac.bali.bom.parts.PartsService;
import ac.bali.bom.supply.Supplier;
import ac.bali.bom.supply.Supply;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.polygene.api.injection.scope.Service;
import org.apache.polygene.api.injection.scope.Structure;
import org.apache.polygene.api.value.ValueBuilder;
import org.apache.polygene.api.value.ValueBuilderFactory;

public abstract class CostEstimationMixin
    implements ProductsService
{
    @Service
    PartsService partsService;

    @Structure
    ValueBuilderFactory vbf;

    @Override
    public CostReport estimateCost(Product product, Integer quantity)
    {
        ValueBuilder<CostReport> builder = vbf.newValueBuilder(CostReport.class);
        CostReport prototype = builder.prototype();
        prototype.dateTime().set(ZonedDateTime.now());
        prototype.name().set(product.name().get() + " - " + product.revision().get());
        BigDecimal total = new BigDecimal(0);
        List<CostItem> itemized = new ArrayList<>();
        List<String> errors = new ArrayList<>();
        for (PartQuantity pq : product.parts().get())
        {
            int totalQuantity = pq.quantity().get() * quantity;
            Part part = pq.part().get();
            Map<String, Supply> supplies = part.supply().get();
            BigDecimal bestPrice = new BigDecimal(1000000000L);
            Supplier supplier = null;
            String spn = null;
            for (Supply supply : supplies.values())
            {
                if( supply != null )
                {
                    BigDecimal price = supply.priceOf(totalQuantity);
                    if (price != null)
                    {
                        if (price.compareTo(bestPrice) < 0)
                        {
                            bestPrice = price;
                            supplier = supply.supplier().get();
                            spn = supply.supplierPartNumber().get();
                        }
                    }
                }
            }
            CostItem costItem = createCostItem(part, supplier, spn, totalQuantity, bestPrice);
            if (costItem != null)
            {
                total = total.add(bestPrice);
                itemized.add(costItem);
            } else
            {
                errors.add("Item " + pq + " can't be fulfilled by any supplier.");
            }
        }
        prototype.total().set(total);
        prototype.itemized().set(itemized);
        prototype.errors().set(errors);
        return builder.newInstance();
    }

    private CostItem createCostItem(Part part, Supplier supplier, String spn, Integer quantity, BigDecimal price)
    {
        if( supplier == null || spn == null || part == null || quantity == null || price == null)
            return null;
        ValueBuilder<CostItem> builder = vbf.newValueBuilder(CostItem.class);
        CostItem prototype = builder.prototype();
        prototype.part().set(part);
        prototype.supplier().set(supplier);
        prototype.supplierPartNumber().set(spn);
        prototype.price().set(price);
        prototype.quantity().set(quantity);
        return builder.newInstance();
    }
}
