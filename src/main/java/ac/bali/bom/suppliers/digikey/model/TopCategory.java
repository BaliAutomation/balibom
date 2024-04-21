package ac.bali.bom.suppliers.digikey.model;

import org.apache.polygene.api.property.Property;

@SuppressWarnings("unused")
public interface TopCategory
{
    Property<TopCategoryNode> RootCategory();
    Property<TopCategoryNode> Category();
    Property<Double> Score();
    Property<String> ImageUrl();
}
