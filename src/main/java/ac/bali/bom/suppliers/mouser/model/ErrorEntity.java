package ac.bali.bom.suppliers.mouser.model;

import org.apache.polygene.api.property.Property;

public interface ErrorEntity
{
    Property<Integer> Id();
    Property<String> Code();
    Property<String> Message();
    Property<String> ResourceKey();
    Property<String> ResourceFormatString();
    Property<String> ResourceFormatString2();
    Property<String> PropertyName();
}
