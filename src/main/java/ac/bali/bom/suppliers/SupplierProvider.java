package ac.bali.bom.suppliers;

import java.util.List;

public interface SupplierProvider
{
    Supply searchSupplierPartNumber(Supplier supplier, String supplierPartNumber);

    Supply searchManufacturerPartNumber(Supplier supplier, String mf, String mpn);

    List<Supply> searchKeywords(Supplier supplier, String keywords);

}
