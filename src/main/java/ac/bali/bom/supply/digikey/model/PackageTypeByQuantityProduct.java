package ac.bali.bom.supply.digikey.model;

import java.util.List;
import org.apache.polygene.api.property.Property;

@SuppressWarnings("unused")
public interface PackageTypeByQuantityProduct
{
    Property<Integer> RecommendedQuantity();

    Property<String> DigiKeyProductNumber();

    Property<Integer> QuantityAvailable();

    Property<String> ProductDescription();

    Property<String> DetailedDescription();

    Property<String> ManufacturerName();

    Property<String> ManufacturerProductNumber();

    Property<Integer> MinimumOrderQuantity();

    Property<String> PrimaryDatasheetUrl();

    Property<String> PrimaryPhotoUrl();

    Property<String> ProductStatus();

    Property<String> ManufacturerLeadWeeks();

    Property<Integer> ManufacturerWarehouseQuantity();

    Property<String> RohsStatus();

    Property<Boolean> RoHSCompliant();

    Property<Integer> QuantityOnOrder();

    Property<List<BreakPrice>> StandardPricing();

    Property<List<BreakPrice>> MyPricing();

    Property<String> ProductUrl();

    Property<Boolean> MarketPlace();

    Property<String> Supplier();

    Property<String> StockNote();

    Property<List<String>> PackageTypes();
}
