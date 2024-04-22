package ac.bali.bom.suppliers.lcsc;

import ac.bali.bom.parts.Price;
import ac.bali.bom.suppliers.Supplier;
import ac.bali.bom.suppliers.Supply;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.UndeclaredThrowableException;
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
import org.apache.polygene.api.mixin.Mixins;
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

@Mixins( LcscSupplier.Mixin.class)
public interface LcscSupplier extends Supplier
{

    abstract class Mixin
        implements LcscSupplier
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

        @Override
        public Supply searchSupplierPartNumber(String spn)
        {
            try (final CloseableHttpClient httpclient = HttpClients.createDefault())
            {
                final HttpGet httpget = new HttpGet(String.format(PRODUCT_FETCH_URL, spn.trim()));
                LcscPart lcscPart = httpclient.execute(httpget, this::convertToPart);
                return createSupply(lcscPart);
            } catch (IOException e)
            {
                throw new UndeclaredThrowableException(e);
            }
        }

        @Override
        public Supply searchManufacturerPartNumber(String mf, String mpn)
        {
            // TODO: LCSC's search is from experience pretty poor and always returns "too much". Let's do this later, if ever.
            return null;
        }

        @Override
        public List<Supply> searchKeywords(String keywords)
        {
            // TODO: LCSC's search is from experience pretty poor and always returns "too much". Let's do this later, if ever.
            return null;
        }

        private Supply createSupply(LcscPart product)
        {
            ValueBuilder<Supply> builder = vbf.newValueBuilder(Supply.class);
            Supply p = builder.prototype();
            p.mf().set(product.brandNameEn().get());
            p.mpn().set(product.productModel().get() );
            p.supplier().set(this);
            p.supplierPartNumber().set(product.productCode().get());
            p.productIntro().set(product.productIntroEn().get());
            p.availableSupply().set(product.shipImmediately().get() + product.ship3Days().get());
            p.inStock().set(product.stockNumber().get());
            p.canShipWithInWeek().set(product.shipImmediately().get() + product.ship3Days().get());
            p.reelSize().set(product.minPacketNumber().get());
            p.isReel().set(product.isReel().get());
            p.images().set(product.productImages().get());
            p.datasheet().set(product.pdfUrl().get());
            p.parameters().set(parametersOf(product));
            p.prices().set(createPriceList(product));
            p.minBuyNumber().set(product.minBuyNumber().get());
            return builder.newInstance();
        }

        private Map<String, String> parametersOf(Object fetched)
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
            LcscPartResponse r = serialization.deserialize(module, LcscPartResponse.class, content);
            if (r.code().get() != 200)
            {
                return null;
            }
            return r.result().get();
        }

        private SortedSet<Price> createPriceList(LcscPart lcscPart)
        {
            TreeSet<Price> result = new TreeSet<>(new Price.PriceComparator());
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

        public static void createSupplier(UnitOfWork uow) throws Exception
        {
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
            }
        }
    }
}