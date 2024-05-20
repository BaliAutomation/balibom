package ac.bali.bom.suppliers.digikey.model;

import org.apache.polygene.api.property.Property;

@SuppressWarnings("unused")
public interface RecommendedProduct
{
    Property<String> DigiKeyProductNumber();

    Property<String> ManufacturerProductNumber();

    Property<String> ManufacturerName();

    Property<String> PrimaryPhoto();

    Property<String> ProductDescription();

    Property<Long> QuantityAvailable();

    Property<Double> UnitPrice();

    Property<String> ProductUrl();
}
