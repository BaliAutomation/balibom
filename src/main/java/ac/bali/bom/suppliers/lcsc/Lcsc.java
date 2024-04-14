package ac.bali.bom.suppliers.lcsc;

import ac.bali.bom.connectivity.PartSupplyConnector;
import ac.bali.bom.parts.Price;
import ac.bali.bom.suppliers.Supplier;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.polygene.api.entity.EntityBuilder;
import org.apache.polygene.api.identity.Identity;
import org.apache.polygene.api.identity.StringIdentity;
import org.apache.polygene.api.injection.scope.Service;
import org.apache.polygene.api.injection.scope.Structure;
import org.apache.polygene.api.query.QueryBuilderFactory;
import org.apache.polygene.api.serialization.Serialization;
import org.apache.polygene.api.structure.ModuleDescriptor;
import org.apache.polygene.api.unitofwork.NoSuchEntityException;
import org.apache.polygene.api.unitofwork.UnitOfWork;
import org.apache.polygene.api.unitofwork.UnitOfWorkFactory;
import org.apache.polygene.api.usecase.Usecase;
import org.apache.polygene.api.usecase.UsecaseBuilder;
import org.apache.polygene.api.value.ValueBuilder;
import org.apache.polygene.api.value.ValueBuilderFactory;

@SuppressWarnings("resource")
public class Lcsc
    implements PartSupplyConnector
{
    private static final String PRODUCT_FETCH_URL = "https://wmsc.lcsc.com/wmsc/product/detail?productCode=%s";

    private static final String SEARCH_URL = "https://wmsc.lcsc.com/wmsc/search/global?keyword=%s";

    private static final String ORDERING_URL = "";
    private static final String WEBSITE_URL = "https://lcsc.com";
    private static final String NAME = "LCSC";
    public static final Identity IDENTITY = StringIdentity.identityOf("Supplier.LCSC");

    @Structure
    private ModuleDescriptor module;

    @Structure
    private ValueBuilderFactory vbf;

    @Structure
    private QueryBuilderFactory qbf;

    @Service
    private Serialization serialization;

    @Structure
    private UnitOfWorkFactory uowf;

    public Lcsc()
    {
        System.out.println("Starting LCSC");
    }

    @Override
    public Object searchSupplierPartNumber(String spn)
    {
        try (final CloseableHttpClient httpclient = HttpClients.createDefault())
        {
            final HttpGet httpget = new HttpGet(String.format(PRODUCT_FETCH_URL, spn.trim()));
            return httpclient.execute(httpget, this::convertToPart);
        } catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String manufacturerOf(Object fetched)
    {
        LcscPart lcscPart = (LcscPart) fetched;
        return lcscPart.brandNameEn().get();
    }

    @Override
    public String mpnOf(Object fetched)
    {
        LcscPart lcscPart = (LcscPart) fetched;
        return lcscPart.productModel().get();
    }

    @Override
    public Integer availableSupplyOf(Object fetched)
    {
        LcscPart lcscPart = (LcscPart) fetched;
        return lcscPart.shipImmediately().get() + lcscPart.ship3Days().get();
    }

    @Override
    public List<String> imagesOf(Object fetched)
    {
        LcscPart lcscPart = (LcscPart) fetched;
        return lcscPart.productImages().get();
    }

    @Override
    public String datasheetOf(Object fetched)
    {
        LcscPart lcscPart = (LcscPart) fetched;
        return lcscPart.pdfUrl().get();
    }

    @Override
    public Map<String, String> parametersOf(Object fetched)
    {
        Map<String, String> result = new HashMap<>();
        LcscPart lcscPart = (LcscPart) fetched;
        lcscPart.paramVOList().get().forEach(p ->
        {
            String name = p.paramNameEn().get();
            String value = p.paramValueEn().get();
            result.put(name, value);
        });
        return result;
    }

    private LcscPart convertToPart(HttpResponse response) throws IOException
    {
        InputStream content = response.getEntity().getContent();
        LcscPartResponse r = parseLcscPartResponse(content);
        if (r.code().get() != 200)
        {
            return null;
        }
        return r.result().get();
    }

    LcscPartResponse parseLcscPartResponse(InputStream content)
    {
        return serialization.deserialize(module, LcscPartResponse.class, content);
    }

    @Override
    public String name()
    {
        return NAME;
    }

    @Override
    public Supplier supplier()
    {
        UnitOfWork uow = uowf.currentUnitOfWork();
        return uow.get(Supplier.class, IDENTITY);
    }

    @Override
    public SortedSet<Price> createPriceList(Object fetched)
    {
        TreeSet<Price> result = new TreeSet<>(new Price.PriceComparator());
        LcscPart lcscPart = (LcscPart) fetched;
        List<LcscPartPrice> prices = lcscPart.productPriceList().get();
        prices.forEach(p ->
        {
            ValueBuilder<Price> builder = vbf.newValueBuilder(Price.class);
            Price prototype = builder.prototype();
            prototype.quantity().set(p.ladder().get());
            prototype.price().set(p.usdPrice().get());
            result.add(builder.newInstance());
        });
        return result;
    }

    @Override
    public Integer reelSizeOf(Object fetched)
    {
        LcscPart lcscPart = (LcscPart) fetched;
        return lcscPart.minPacketNumber().get();
    }

    public Integer inStock(Object fetched)
    {
        LcscPart lcscPart = (LcscPart) fetched;
        return lcscPart.stockNumber().get();
    }

    public Boolean isReel(Object fetched)
    {
        LcscPart lcscPart = (LcscPart) fetched;
        return lcscPart.isReel().get();
    }

    public Integer canShipWithInWeek(Object fetched)
    {
        LcscPart lcscPart = (LcscPart) fetched;
        return lcscPart.shipImmediately().get() + lcscPart.ship3Days().get();
    }

    public Integer minBuyNumber(Object fetched)
    {
        LcscPart lcscPart = (LcscPart) fetched;
        return lcscPart.minBuyNumber().get();
    }

    @Override
    public void activateService() throws Exception
    {
        Usecase usecase = UsecaseBuilder.newUsecase("LCSC Start");
        UnitOfWork uow = uowf.newUnitOfWork(usecase);
        try
        {
            uow.get(Supplier.class, IDENTITY);
        } catch (NoSuchEntityException e)
        {
            EntityBuilder<Supplier> builder = uow.newEntityBuilder(Supplier.class, IDENTITY);
            Supplier instance = builder.instance();
            instance.name().set("LCSC");
            instance.productDetailsApi().set(PRODUCT_FETCH_URL);
            instance.orderingApi().set(ORDERING_URL);
            instance.searchApi().set(SEARCH_URL);
            instance.website().set(WEBSITE_URL);
            instance.bomColumns().get().add(NAME);
            builder.newInstance();
            uow.complete();
        } finally
        {
            uow.discard();
        }
    }

    @Override
    public void passivateService() throws Exception
    {

    }
}
