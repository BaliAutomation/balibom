package ac.bali.bom.suppliers.mouser;

import ac.bali.bom.parts.Price;
import ac.bali.bom.suppliers.Supplier;
import ac.bali.bom.suppliers.SupplierProvider;
import ac.bali.bom.suppliers.Supply;
import ac.bali.bom.suppliers.mouser.model.MouserPart;
import ac.bali.bom.suppliers.mouser.model.Pricebreak;
import ac.bali.bom.suppliers.mouser.model.ProductAttribute;
import ac.bali.bom.suppliers.mouser.model.SearchByPartMfrNameRequest;
import ac.bali.bom.suppliers.mouser.model.SearchByPartMfrNameRequestRoot;
import ac.bali.bom.suppliers.mouser.model.SearchResponseRoot;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import org.apache.polygene.api.entity.EntityBuilder;
import org.apache.polygene.api.identity.Identity;
import org.apache.polygene.api.identity.StringIdentity;
import org.apache.polygene.api.injection.scope.Service;
import org.apache.polygene.api.injection.scope.Structure;
import org.apache.polygene.api.mixin.Mixins;
import org.apache.polygene.api.unitofwork.NoSuchEntityException;
import org.apache.polygene.api.unitofwork.UnitOfWork;
import org.apache.polygene.api.unitofwork.UnitOfWorkFactory;
import org.apache.polygene.api.value.ValueBuilder;
import org.apache.polygene.api.value.ValueBuilderFactory;

@Mixins(MouserSupplier.Mixin.class)
public interface MouserSupplier extends SupplierProvider
{
    String HOST = "host";
    String HOSTNAME = "https://api.mouser.com";
    String PRODUCTS = "products";
    String PRODUCT_DETAILS_API_PATH = "/api/v2/search/partnumberandmanufacturer?apiKey=${apiKey}";
    String SEARCH = "search";
    String SEARCH_API_PATH = "/api/v2/search";
    String WEBSITE = "https://eu.mouser.com/";

    class Mixin
        implements MouserSupplier
    {
        private static final String NAME = "Mouser";
        public static final Identity IDENTITY = StringIdentity.identityOf("Supplier.Mouser");


        @Structure
        UnitOfWorkFactory uowf;

        @Structure
        ValueBuilderFactory vbf;

        @Service
        ProductSearchApi products;

        @Override
        public Supply searchSupplierPartNumber(Supplier supplier, String supplierPartNumber)
        {
            return null;
        }

        @Override
        public Supply searchManufacturerPartNumber(Supplier supplier, String mf, String mpn)
        {
            if (mf == null || mf.trim().length() == 0)
            {
                return null;
            }
            if (mpn == null || mpn.trim().length() == 0)
            {
                return null;
            }
            ValueBuilder<SearchByPartMfrNameRequest> builder2 =
                vbf.newValueBuilder(SearchByPartMfrNameRequest.class);
            SearchByPartMfrNameRequest prototype2 = builder2.prototype();
            prototype2.manufacturerName().set(mf);
            prototype2.mouserPartNumber().set(mpn);
            prototype2.partSearchOptions().set("Exact");

            ValueBuilder<SearchByPartMfrNameRequestRoot> builder3 =
                vbf.newValueBuilder(SearchByPartMfrNameRequestRoot.class);
            SearchByPartMfrNameRequestRoot prototype3 = builder3.prototype();
            prototype3.SearchByPartMfrNameRequest().set(builder2.newInstance());
            SearchByPartMfrNameRequestRoot search = builder3.newInstance();

            SearchResponseRoot response = products.searchByPartMfrName(supplier, search);
            return createSupply(supplier, response).get(0);
        }

        @Override
        public List<Supply> searchKeywords(Supplier supplier, String keywords)
        {
            return null;
        }

        private List<Supply> createSupply(Supplier supplier, SearchResponseRoot product)
        {
            List<Supply> supplies = new ArrayList<>();
            for (MouserPart mp : product.SearchResults().get().Parts().get())
            {
                ValueBuilder<Supply> builder = vbf.newValueBuilder(Supply.class);
                Supply p = builder.prototype();
                p.mf().set(mp.ActualMfrName().get());
                p.mpn().set(mp.ManufacturerPartNumber().get());
                p.supplier().set(supplier);
                p.supplierPartNumber().set(mp.MouserPartNumber().get());
                p.productIntro().set(mp.Description().get());
                p.availableSupply().set(Integer.valueOf(mp.AvailableOnOrder().get()));
                p.inStock().set(Integer.valueOf(mp.AvailabilityInStock().get()));
                p.canShipWithInWeek().set(mp.AvailabilityOnOrder().get().get(0).Quantity().get());
                p.reelSize().set(Integer.valueOf(mp.FactoryStock().get()));
                p.isReel().set(mp.Reeling().get());
                p.images().set(List.of(mp.ImagePath().get()));
                p.datasheet().set(mp.DataSheetUrl().get());
                p.parameters().set(getParameters(mp.ProductAttributes().get()));
                p.prices().set(getPriceList(mp.PriceBreaks().get()));
                p.minBuyNumber().set(Integer.valueOf(mp.Min().get()));
                supplies.add(builder.newInstance());
            }
            return supplies;
        }

        private SortedSet<Price> getPriceList(List<Pricebreak> prices)
        {
            TreeSet<Price> result = new TreeSet<>(new Price.PriceComparator());
            prices.forEach(p ->
            {
                ValueBuilder<Price> builder = vbf.newValueBuilder(Price.class);
                Price prototype = builder.prototype();
                prototype.quantity().set(p.Quantity().get());
                prototype.price().set(new BigDecimal(p.Price().get()));
                result.add(builder.newInstance());
            });
            return result;
        }

        private static Map<String, String> getParameters(List<ProductAttribute> attributes)
        {
            Map<String, String> result = new HashMap<>();
            for (ProductAttribute p : attributes)
            {
                String k = p.AttributeName().get();
                String v = p.AttributeValue().get();
                result.put(k, v);
            }
            return result;
        }

        public static void createSupplier(UnitOfWork uow) throws Exception
        {
            try
            {
                uow.get(Supplier.class, IDENTITY);
            } catch (NoSuchEntityException e)
            {
                EntityBuilder<Supplier> eb = uow.newEntityBuilder(Supplier.class, IDENTITY);
                Supplier instance = eb.instance();
                instance.name().set("Mouser");

                //noinspection DuplicatedCode
                Map<String, String> hosts = new HashMap<>();
                hosts.put(HOST, HOSTNAME);
                instance.hosts().set(hosts);

                Map<String, String> paths = new HashMap<>();
                paths.put(PRODUCTS, PRODUCT_DETAILS_API_PATH);
                paths.put(SEARCH, SEARCH_API_PATH);
                instance.paths().set(paths);

                instance.website().set(WEBSITE);
                instance.bomColumns().get().add(NAME);
                instance.bomColumns().get().add("Mouser_PN");
                instance.bomColumns().get().add("MouserPN");
                instance.bomColumns().get().add("Mouser-PN");
                instance.enabled().set(false);
                eb.newInstance();
            }
        }
    }
}
