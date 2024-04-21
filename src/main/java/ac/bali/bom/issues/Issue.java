package ac.bali.bom.issues;

import org.apache.polygene.api.property.Property;

public interface Issue
{
    Property<String> topic();
    Property<String> issue();
}
