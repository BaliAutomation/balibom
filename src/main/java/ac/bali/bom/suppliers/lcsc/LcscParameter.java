package ac.bali.bom.suppliers.lcsc;

import org.apache.polygene.api.common.Optional;
import org.apache.polygene.api.property.Property;

public interface LcscParameter {

    Property<String> paramCode();

    Property<String> paramName();

    Property<String> paramNameEn();

    Property<String> paramValue();

    Property<String> paramValueEn();

    @Optional
    Property<String> paramValueEnForSearch();

    Property<Boolean> isMain();

    Property<Integer> sortNumber();

}
