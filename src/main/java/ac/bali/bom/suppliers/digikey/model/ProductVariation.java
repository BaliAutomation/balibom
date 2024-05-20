package ac.bali.bom.suppliers.digikey.model;

import java.util.List;
import org.apache.polygene.api.property.Property;

@SuppressWarnings("unused")
public interface ProductVariation
{
    Property<String> DigiKeyProductNumber();

    Property<PackageType> PackageType();

    Property<List<PriceBreak>> StandardPricing();

    Property<List<PriceBreak>> MyPricing();

    Property<Boolean> MarketPlace();

    Property<Boolean> TariffActive();

    Property<Supplier> Supplier();

    Property<Integer> QuantityAvailableforPackageType();

    Property<Long> MaxQuantityForDistribution();

    Property<Integer> MinimumOrderQuantity();

    Property<Integer> StandardPackage();

    Property<Double> DigiReelFee();
}
