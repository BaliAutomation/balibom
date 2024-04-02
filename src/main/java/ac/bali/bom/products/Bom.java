package ac.bali.bom.products;

import java.util.List;
import java.util.Map;

import org.apache.polygene.api.identity.HasIdentity;
import org.apache.polygene.api.property.Property;

public interface Bom
{
    Property<String> product();
    Property<String> revision();
    Property<List<BomItem>> items();
}
