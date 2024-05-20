package ac.bali.bom.suppliers.digikey.model;

import org.apache.polygene.api.common.Optional;
import org.apache.polygene.api.common.UseDefaults;
import org.apache.polygene.api.property.Property;

@SuppressWarnings("unused")
public interface Classifications
{
    @UseDefaults("")
    Property<String> ReachStatus();

    @UseDefaults("")
    Property<String> RohsStatus();

    @UseDefaults("") @Optional
    Property<String> MoistureSensitivityLevel();

    @UseDefaults("")
    Property<String> ExportControlClassNumber();

    @UseDefaults("")
    Property<String> HtsusCode();
}
