package ac.bali.bom.connectivity;

import ac.bali.bom.parts.Price;
import ac.bali.bom.suppliers.Supplier;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import org.apache.polygene.api.service.ServiceActivation;

public interface PartSupplyConnector extends ServiceActivation
{

    Object searchSupplierPartNumber(String supplierPartNumber);


    String manufacturerOf(Object fetched);

    String mpnOf(Object fetched);

    Integer availableSupplyOf(Object fetched);

    Integer reelSizeOf(Object fetched);

    List<String> imagesOf(Object fetched);

    String datasheetOf(Object fetched);

    Map<String, String> parametersOf(Object fetched);

    String name();

    Supplier supplier();

    SortedSet<Price> createPriceList(Object fetched);

    Integer inStock(Object fetched);

    Boolean isReel(Object fetched);

    Integer canShipWithInWeek(Object fetched);

    Integer minBuyNumber(Object fetched);
}
