package ac.bali.bom.suppliers.digikey.model;

import org.apache.polygene.api.property.Property;

@SuppressWarnings("unused")
public interface Classifications
{
    Property<String> ReachStatus();
    Property<String> RohsStatus();
    Property<String> MoistureSensitivityLevel();
    Property<String> ExportControlClassNumber();
    Property<String> HtsusCode();
}
