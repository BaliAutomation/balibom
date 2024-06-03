package ac.bali.bom.supply.digikey.model;

import org.apache.polygene.api.property.Property;

@SuppressWarnings("unused")
public interface Description
{
    Property<String> ProductDescription();

    Property<String> DetailedDescription();
}
