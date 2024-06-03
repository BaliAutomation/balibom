package ac.bali.bom.supply.digikey.model;

import org.apache.polygene.api.property.Property;

@SuppressWarnings("unused")
public interface DigiReelPricing
{
    Property<Double> ReelingFee();

    Property<Double> UnitPrice();

    Property<Double> ExtendedPrice();

    Property<Integer> RequestedQuantity();

    Property<IsoSearchLocale> SearchLocaleUsed();
}
