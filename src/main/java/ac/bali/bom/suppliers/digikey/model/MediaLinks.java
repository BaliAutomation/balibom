package ac.bali.bom.suppliers.digikey.model;

import org.apache.polygene.api.property.Property;

@SuppressWarnings("unused")
public interface MediaLinks
{
    Property<String> MediaType();

    Property<String> Title();

    Property<String> SmallPhoto();

    Property<String> Thumbnail();

    Property<String> Url();
}
