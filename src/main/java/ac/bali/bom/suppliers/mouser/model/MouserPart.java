package ac.bali.bom.suppliers.mouser.model;

import java.util.List;
import org.apache.polygene.api.property.Property;

public interface MouserPart
{
    Property<String> Availability();
    Property<String> DataSheetUrl();
    Property<String> Description();
    Property<String> FactoryStock();
    Property<String> ImagePath();
    Property<String> Category();
    Property<String> LeadTime();
    Property<String> LifecycleStatus();
    Property<String> Manufacturer();
    Property<String> ManufacturerPartNumber();
    Property<String> Min();
    Property<String> Mult();
    Property<String> MouserPartNumber();
    Property<List<ProductAttribute>> ProductAttributes();
    Property<List<Pricebreak>> PriceBreaks();
    Property<List<AlternatePackaging>> AlternatePackagings();
    Property<String> ProductDetailUrl();
    Property<Boolean> Reeling();
    Property<String> ROHSStatus();

//    Property<List<String>> REACH-SVHC();
    Property<String> SuggestedReplacement();
    Property<Integer> MultiSimBlue();
    Property<UnitWeightKg> UnitWeightKg();
    Property<StandardCost> StandardCost();
    Property<String> IsDiscontinued();
    Property<String> RTM();
    Property<String> MouserProductCategory();
    Property<String> IPCCode();
    Property<String> SField();
    Property<String> VNum();
    Property<String> ActualMfrName();
    Property<String> AvailableOnOrder();
    Property<String> AvailabilityInStock();
    Property<List<AvailabilityOnOrderObject>> AvailabilityOnOrder();
    Property<List<String>> InfoMessages();
    Property<String> SalesMaximumOrderQty();
    Property<String> RestrictionMessage();
    Property<String> PID();
    Property<List<ProductCompliance>> ProductCompliance();
}
