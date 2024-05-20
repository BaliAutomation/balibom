package ac.bali.bom.suppliers.digikey.model;

import org.apache.polygene.api.property.Property;

@SuppressWarnings("unused")
public interface ParameterValue
{
    Property<Integer> ParameterId();

    Property<String> ParameterText();

    Property<ParameterTypeEnum> ParameterType();

    Property<String> ValueId();

    Property<String> ValueText();
}
