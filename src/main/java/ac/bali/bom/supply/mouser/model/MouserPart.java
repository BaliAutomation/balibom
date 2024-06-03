package ac.bali.bom.supply.mouser.model;

import java.util.List;
import org.apache.polygene.api.common.Optional;
import org.apache.polygene.api.property.Property;

public interface MouserPart
{
    @Optional
    Property<String> Availability();

    Property<String> DataSheetUrl();

    Property<String> Description();

    @Optional
    Property<String> FactoryStock();

    @Optional
    Property<String> ImagePath();

    Property<String> Category();

    Property<String> LeadTime();

    @Optional
    Property<String> LifecycleStatus();

    Property<String> Manufacturer();

    Property<String> ManufacturerPartNumber();

    Property<String> Min();

    Property<String> Mult();

    Property<String> MouserPartNumber();

    Property<List<ProductAttribute>> ProductAttributes();

    Property<List<Pricebreak>> PriceBreaks();

    @Optional
    Property<List<AlternatePackaging>> AlternatePackagings();

    Property<String> ProductDetailUrl();

    Property<Boolean> Reeling();

    Property<String> ROHSStatus();

    //    Property<List<String>> REACH-SVHC();
    Property<String> SuggestedReplacement();

    Property<Integer> MultiSimBlue();

    @Optional
    Property<UnitWeightKg> UnitWeightKg();

    @Optional
    Property<StandardCost> StandardCost();

    @Optional
    Property<String> IsDiscontinued();

    @Optional
    Property<String> RTM();

    @Optional
    Property<String> MouserProductCategory();

    @Optional
    Property<String> IPCCode();

    @Optional
    Property<String> SField();

    @Optional
    Property<String> VNum();

    @Optional
    Property<String> ActualMfrName();

    @Optional
    Property<List<String>> AvailableOnOrder();

    @Optional
    Property<String> AvailabilityInStock();

    Property<List<AvailabilityOnOrderObject>> AvailabilityOnOrder();

    Property<List<String>> InfoMessages();

    @Optional
    Property<String> SalesMaximumOrderQty();

    @Optional
    Property<String> RestrictionMessage();

    @Optional
    Property<String> PID();

    Property<List<ProductCompliance>> ProductCompliance();
}
