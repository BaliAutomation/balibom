package ac.bali.bom.suppliers.digikey.model;

import org.apache.polygene.api.property.Property;

@SuppressWarnings("unused")
public interface CategoryResponse
{
    Property<Category> Category();

    Property<IsoSearchLocale> SearchLocaleUsed();
}
