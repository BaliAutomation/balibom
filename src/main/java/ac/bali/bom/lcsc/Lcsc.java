package ac.bali.bom.lcsc;

import ac.bali.bom.connectivity.PartSupplyConnector;
import ac.bali.bom.parts.Manufacturer;
import ac.bali.bom.parts.ManufacturersService;
import ac.bali.bom.parts.Part;
import ac.bali.bom.parts.Supply;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.polygene.api.injection.scope.Service;
import org.apache.polygene.api.injection.scope.Structure;
import org.apache.polygene.api.serialization.Serialization;
import org.apache.polygene.api.structure.ModuleDescriptor;
import org.apache.polygene.api.value.ValueBuilder;
import org.apache.polygene.api.value.ValueBuilderFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

public class Lcsc
    implements PartSupplyConnector
{
    private static final String BASE_URL = "https://wmsc.lcsc.com/wmsc/product/detail?productCode=%s";

    @Structure
    private ModuleDescriptor module;

    @Structure
    private ValueBuilderFactory vbf;

    @Service
    private Serialization serialization;

    @Service
    private ManufacturersService manufacturersService;

    public Part findPart(String lcscCode)
    {
        try( final CloseableHttpClient httpclient = HttpClients.createDefault() )
        {
            final HttpGet httpget = new HttpGet( String.format( BASE_URL, lcscCode.trim() ) );
            return httpclient.execute( httpget, response ->
            {
                LcscResponse r = serialization.deserialize(module, LcscResponse.class, response.getEntity().getContent());
                if( r.code().get() != 200 )
                {
                    return null;
                }
                LcscPart lcscPart = r.result().get();
                ValueBuilder<Part> partBuilder = vbf.newValueBuilder(Part.class);
                Manufacturer manufacturer = manufacturersService.findManufacturer(lcscPart.brandNameEn().get());

                Part partPrototype = partBuilder.prototype();
                partPrototype.manufacturer().set(manufacturer);
                partPrototype.mpn().set(lcscPart.productModel().get());

                ValueBuilder<Supply> supplyBuilder = vbf.newValueBuilder(Supply.class);
                Supply supplyPrototype = supplyBuilder.prototype();
                supplyPrototype.partNumber().set(lcscPart.productCode().get());
                supplyPrototype.availableSupply().set(lcscPart.shipImmediately().get() + lcscPart.ship3Days().get());
                supplyPrototype.prices().set(createPriceList(lcscPart.productPriceList().get()));
                supplyPrototype.reelSize().set(lcscPart.minPacketNumber().get());
                Supply supply = supplyBuilder.newInstance();

                partPrototype.supply().put("lcsc", supply);
                Part part = partBuilder.newInstance();
                return part;
            } );
        }
        catch( IOException e )
        {
            e.printStackTrace();
            return null;
        }
    }

    private SortedMap<Integer, BigDecimal> createPriceList(List<LcscPartPrice> lcscPartPrices) {

        SortedMap<Integer, BigDecimal> result = new TreeMap<>();
        for(LcscPartPrice pp : lcscPartPrices )
        {
            int quantity = pp.ladder().get();
            BigDecimal price = pp.usdPrice().get();
            result.put(quantity, price);
        }
        return result;
    }

    @Override
    public List<Supply> search(String freetext) {
        return Collections.emptyList();
    }
}
