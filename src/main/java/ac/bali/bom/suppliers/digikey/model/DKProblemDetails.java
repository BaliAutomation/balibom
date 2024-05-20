package ac.bali.bom.suppliers.digikey.model;

import java.util.List;
import java.util.Map;
import org.apache.polygene.api.property.Property;

@SuppressWarnings("unused")
public interface DKProblemDetails
{
    Property<String> type();

    Property<String> title();

    Property<Integer> status();

    Property<String> detail();

    Property<String> instance();

    Property<String> correlationId();

    Property<Map<String, List<String>>> errors();
}
