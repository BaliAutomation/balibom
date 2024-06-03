package ac.bali.bom.supply.digikey.model;

import org.apache.polygene.api.common.UseDefaults;
import org.apache.polygene.api.property.Property;

@SuppressWarnings("unused")
public interface SortOptions
{
    @UseDefaults("None")
    Property<FieldEnum> Field();

    @UseDefaults("Ascending")
    Property<SortOrderEnum> SortOrder();
}
