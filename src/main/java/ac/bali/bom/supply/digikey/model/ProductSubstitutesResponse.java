package ac.bali.bom.supply.digikey.model;

import java.util.List;
import org.apache.polygene.api.property.Property;

@SuppressWarnings("unused")
public interface ProductSubstitutesResponse
{
    Property<Integer> ProductSubstitutesCount();

    Property<List<ProductSubstitute>> ProductSubstitutes();

    Property<IsoSearchLocale> SearchLocaleUsed();
}
