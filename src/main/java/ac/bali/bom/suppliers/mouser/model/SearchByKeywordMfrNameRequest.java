package ac.bali.bom.suppliers.mouser.model;

import org.apache.polygene.api.property.Property;

public interface SearchByKeywordMfrNameRequest
{
    Property<String> manufacturerName();
    Property<String> keyword();
    Property<Integer> records();
    Property<Integer> pageNumber();
    Property<String> searchOptions();
    Property<String> searchWithYourSignUpLanguage();
}
