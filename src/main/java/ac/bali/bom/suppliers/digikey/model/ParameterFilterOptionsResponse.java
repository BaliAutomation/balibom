package ac.bali.bom.suppliers.digikey.model;

import java.util.List;
import org.apache.polygene.api.property.Property;

@SuppressWarnings("unused")
public interface ParameterFilterOptionsResponse
{
    Property<BaseFilterV4> Category();

    Property<String> ParameterType();

    Property<Integer> ParameterId();

    Property<String> ParameterName();

    Property<List<FilterValue>> FilterValues();
}
