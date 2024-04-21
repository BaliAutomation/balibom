package ac.bali.bom.suppliers.digikey.model;

import java.util.List;
import org.apache.polygene.api.property.Property;

@SuppressWarnings("unused")
public interface CategoryNode
{
    Property<Integer> CategoryId();
    Property<Integer> ParentId();
    Property<String> Name();
    Property<Long> ProductCount();
    Property<Long> NewProductCount();
    Property<String> ImageUrl();
    Property<String> SeoDescription();
    Property<List<CategoryNode>> ChildCategories();
}
