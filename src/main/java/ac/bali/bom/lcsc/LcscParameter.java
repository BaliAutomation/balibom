package ac.bali.bom.lcsc;

import org.apache.polygene.api.property.Property;

public interface LcscParameter {

    Property<String> paramCode();

    Property<String> paramName();

    Property<String> paramNameEn();

    Property<String> paramValue();

    Property<String> paramValueEn();

    Property<String> paramValueEnForSearch();

    Property<Boolean> isMain();

    Property<Integer> sortNumber();

}
