package ac.bali.bom.supply.digikey.model;

import java.time.OffsetDateTime;
import java.util.List;
import org.apache.polygene.api.common.Optional;
import org.apache.polygene.api.property.Property;

@SuppressWarnings("unused")
public interface Product
{
    Property<Description> Description();

    Property<Manufacturer> Manufacturer();

    Property<String> ManufacturerProductNumber();

    Property<Double> UnitPrice();

    Property<String> ProductUrl();

    Property<String> DatasheetUrl();

    Property<String> PhotoUrl();

    Property<List<ProductVariation>> ProductVariations();

    Property<Long> QuantityAvailable();

    Property<ProductStatusV4> ProductStatus();

    Property<Boolean> BackOrderNotAllowed();

    Property<Boolean> NormallyStocking();

    Property<Boolean> Discontinued();

    Property<Boolean> EndOfLife();

    Property<Boolean> Ncnr();

    @Optional
    Property<String> PrimaryVideoUrl();

    Property<List<ParameterValue>> Parameters();

    @Optional
    Property<BaseProduct> BaseProductNumber();

    Property<CategoryNode> Category();

    @Optional
    Property<OffsetDateTime> DateLastBuyChance();

    Property<String> ManufacturerLeadWeeks();

    Property<Integer> ManufacturerPublicQuantity();

    Property<Series> Series();

    @Optional
    Property<String> ShippingInfo();

    Property<Classifications> Classifications();
}
