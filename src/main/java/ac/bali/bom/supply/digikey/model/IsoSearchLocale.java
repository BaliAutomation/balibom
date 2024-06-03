package ac.bali.bom.supply.digikey.model;

import org.apache.polygene.api.property.Property;

@SuppressWarnings("unused")
public interface IsoSearchLocale
{
    Property<String> Site();

    Property<String> Language();

    Property<String> Currency();
}
