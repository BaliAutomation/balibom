package ac.bali.bom.supply.digikey.model;

import java.util.List;
import org.apache.polygene.api.property.Property;

@SuppressWarnings("unused")
public interface Category
{
    Property<Integer> CategoryId();

    Property<Integer> ParentId();

    Property<String> Name();

    Property<Long> ProductCount();

    Property<List<Category>> Children();
}
