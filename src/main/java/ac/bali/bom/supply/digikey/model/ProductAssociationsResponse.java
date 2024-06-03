package ac.bali.bom.supply.digikey.model;

import org.apache.polygene.api.property.Property;

@SuppressWarnings("unused")
public interface ProductAssociationsResponse
{
    Property<ProductAssociations> ProductAssociations();

    Property<IsoSearchLocale> SearchLocaleUsed();
}
