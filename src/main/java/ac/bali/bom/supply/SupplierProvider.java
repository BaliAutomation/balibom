package ac.bali.bom.supply;

import java.util.List;
import java.util.Map;

public interface SupplierProvider
{
    Supply searchSupplierPartNumber(Supplier supplier, String supplierPartNumber, Map<String,String> attributes);

    Supply searchManufacturerPartNumber(Supplier supplier, String mf, String mpn, Map<String,String> attributes);

    List<Supply> searchKeywords(Supplier supplier, String keywords);

}
