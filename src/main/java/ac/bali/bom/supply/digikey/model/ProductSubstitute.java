package ac.bali.bom.supply.digikey.model;

import org.apache.polygene.api.property.Property;

@SuppressWarnings("unused")
public interface ProductSubstitute
{
    Property<String> SubstituteType();

    Property<String> ProductUrl();

    Property<String> Description();

    Property<Manufacturer> Manufacturer();

    Property<String> ManufacturerProductNumber();

    Property<String> UnitPrice();

    Property<Integer> QuantityAvailable();
}
