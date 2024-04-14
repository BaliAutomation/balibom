package ac.bali.bom.suppliers.digikey;

import ac.bali.bom.connectivity.PartSupplyConnector;
import ac.bali.bom.parts.Price;
import ac.bali.bom.suppliers.Supplier;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import org.apache.polygene.api.service.ServiceActivation;

public class DigikeySupplier
    implements PartSupplyConnector, ServiceActivation
{

    @Override
    public Object searchSupplierPartNumber(String supplierPartNumber)
    {
        return null;
    }

    @Override
    public String manufacturerOf(Object fetched)
    {
        return null;
    }

    @Override
    public String mpnOf(Object fetched)
    {
        return null;
    }

    @Override
    public Integer availableSupplyOf(Object fetched)
    {
        return null;
    }

    @Override
    public Integer reelSizeOf(Object fetched)
    {
        return null;
    }

    @Override
    public List<String> imagesOf(Object fetched)
    {
        return null;
    }

    @Override
    public String datasheetOf(Object fetched)
    {
        return null;
    }

    @Override
    public Map<String, String> parametersOf(Object fetched)
    {
        return null;
    }

    @Override
    public String name()
    {
        return null;
    }

    @Override
    public Supplier supplier()
    {
        return null;
    }

    @Override
    public SortedSet<Price> createPriceList(Object fetched)
    {
        return null;
    }

    @Override
    public Integer inStock(Object fetched)
    {
        return null;
    }

    @Override
    public Boolean isReel(Object fetched)
    {
        return null;
    }

    @Override
    public Integer canShipWithInWeek(Object fetched)
    {
        return null;
    }

    @Override
    public Integer minBuyNumber(Object fetched)
    {
        return null;
    }

    @Override
    public void activateService() throws Exception
    {

    }

    @Override
    public void passivateService() throws Exception
    {

    }
}
