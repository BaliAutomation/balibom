package ac.bali.bom.supply.digikey.model;

import org.apache.polygene.api.property.Property;

@SuppressWarnings("unused")
public interface ProductDetails
{
    Property<IsoSearchLocale> SearchLocaleUsed();

    Property<Product> Product();
}
