package ac.bali.bom.suppliers.digikey.model;

import org.apache.polygene.api.property.Property;

@SuppressWarnings("unused")
public interface BreakPrice
{
    Property<Double> Price();

    Property<Integer> Quantity();
}
