package ac.bali.bom.supply.digikey.model;

import java.util.List;
import org.apache.polygene.api.property.Property;

@SuppressWarnings("unused")
public interface ParameterFilterRequest
{
    Property<FilterId> CategoryFilter();

    Property<List<ParametricCategory>> ParameterFilters();
}