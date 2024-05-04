package ac.bali.bom.suppliers.mouser.model;

import org.apache.polygene.api.common.Optional;
import org.apache.polygene.api.property.Property;

public interface ErrorEntity
{
    Property<Integer> Id();

    Property<String> Code();

    Property<String> Message();

    Property<String> ResourceKey();

    @Optional
    Property<String> ResourceFormatString();

    @Optional
    Property<String> ResourceFormatString2();

    @Optional
    Property<String> PropertyName();
}
