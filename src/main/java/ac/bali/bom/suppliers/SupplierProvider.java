package ac.bali.bom.suppliers;

import java.util.List;

public interface SupplierProvider
{
    Supply searchSupplierPartNumber(String supplierPartNumber);

    Supply searchManufacturerPartNumber(String mf, String mpn);

    List<Supply> searchKeywords(String keywords);

}
