package ac.bali.bom.supply.mouser.model;

import org.apache.polygene.api.property.Property;

public interface Pricebreak
{
    Property<Integer> Quantity();
    Property<String> Price();
    Property<String> Currency();
}
