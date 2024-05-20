package ac.bali.bom.suppliers.mouser;

import ac.bali.bom.manufacturers.Manufacturer;
import ac.bali.bom.manufacturers.ManufacturersService;
import ac.bali.bom.parts.Price;
import ac.bali.bom.suppliers.Supplier;
import ac.bali.bom.suppliers.SupplierProvider;
import ac.bali.bom.suppliers.Supply;
import ac.bali.bom.suppliers.apikeyauth.ApiKeyAuthentication;
import ac.bali.bom.suppliers.mouser.model.AvailabilityOnOrderObject;
import ac.bali.bom.suppliers.mouser.model.MouserPart;
import ac.bali.bom.suppliers.mouser.model.Pricebreak;
import ac.bali.bom.suppliers.mouser.model.ProductAttribute;
import ac.bali.bom.suppliers.mouser.model.SearchByPartMfrNameRequest;
import ac.bali.bom.suppliers.mouser.model.SearchByPartMfrNameRequestRoot;
import ac.bali.bom.suppliers.mouser.model.SearchResponse;
import ac.bali.bom.suppliers.mouser.model.SearchResponseRoot;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
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
import org.apache.polygene.api.property.Property;
import org.apache.polygene.api.unitofwork.NoSuchEntityException;
import org.apache.polygene.api.unitofwork.UnitOfWork;
import org.apache.polygene.api.unitofwork.UnitOfWorkFactory;
import org.apache.polygene.api.value.ValueBuilder;
import org.apache.polygene.api.value.ValueBuilderFactory;
import org.apache.polygene.spi.PolygeneSPI;

@Mixins(MouserSupplier.Mixin.class)
public interface MouserSupplier extends SupplierProvider
{
    String HOST = "host";
    String HOSTNAME = "https://api.mouser.com";
    String PRODUCTS = "products";
    String PRODUCT_DETAILS_API_PATH = "/api/v2/search/partnumberandmanufacturer?apiKey=${apiKey}";
    String SEARCH = "search";
    String SEARCH_API_PATH = "/api/v2/search/keywordandmanufacturer";
    String MANUFACTURER_LIST = "manufacturerList";
    String MANUFACTURER_LIST_PATH = "/api/v2/search/manufacturerlist";
    String WEBSITE = "https://eu.mouser.com/";

    class Mixin
        implements MouserSupplier
    {
        private static final String NAME = "Mouser";
        public static final Identity IDENTITY = StringIdentity.identityOf("Supplier.Mouser");
        public static final Identity AUTH_IDENTITY = StringIdentity.identityOf("Authentication.Mouser");


        @Structure
        PolygeneSPI spi;

        @Structure
        UnitOfWorkFactory uowf;

        @Structure
        ValueBuilderFactory vbf;

        @Service
        ProductSearchApi products;

        @Service
        ManufacturersService manufacturers;

        @Override
        public Supply searchSupplierPartNumber(Supplier supplier, String supplierPartNumber, Map<String,String> attributes)
        {
            return null;
        }

        @Override
        public Supply searchManufacturerPartNumber(Supplier supplier, String mf, String mpn, Map<String,String> attributes)
        {
            if (mf == null || mf.trim().length() == 0)
            {
                return null;
            }
            if (mpn == null || mpn.trim().length() == 0)
            {
                return null;
            }
            ValueBuilder<SearchByPartMfrNameRequest> builder2 = vbf.newValueBuilder(SearchByPartMfrNameRequest.class);
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
            List<Supply> supply = createSupply(supplier, response);
            if (supply.size() > 1)
                System.err.println("WARNING: Mouser: More than one search result for " + mf + " " + mpn);
            if (supply.size() == 0)
                return null;
            return supply.get(0);
        }

        @Override
        public List<Supply> searchKeywords(Supplier supplier, String keywords)
        {
            return null;
        }

        private List<Supply> createSupply(Supplier supplier, SearchResponseRoot product)
        {
            List<Supply> supplies = new ArrayList<>();
            SearchResponse searchResponse = product.SearchResults().get();
            if (searchResponse != null)
            {
                for (MouserPart mp : searchResponse.Parts().get())
                {
                    ValueBuilder<Supply> builder = vbf.newValueBuilder(Supply.class);
                    Supply p = builder.prototype();
                    p.updatedOn().set(LocalDate.now());
                    Manufacturer mf = manufacturers.findManufacturer(mp.Manufacturer().get());
                    p.mf().set(mf);
                    p.mpn().set(mp.ManufacturerPartNumber().get());
                    p.supplier().set(supplier);
                    p.supplierPartNumber().set(mp.MouserPartNumber().get());
                    p.productIntro().set(mp.Description().get());
                    Integer available = integerOf(mp.AvailabilityInStock(), 0);
                    p.availableSupply().set(available);
                    p.inStock().set(available);
                    int availableWithinWeek = computeWithinWeek(mp);
                    p.canShipWithInWeek().set(availableWithinWeek);
                    p.reelSize().set(reelSizeOf(mp));
                    p.isReel().set(mp.Reeling().get());
                    String imagePath = mp.ImagePath().get();
                    p.images().set(singleItemListOf(imagePath));
                    p.datasheet().set(mp.DataSheetUrl().get());
                    p.parameters().set(getParameters(mp.ProductAttributes().get()));
                    p.prices().set(getPriceList(mp.PriceBreaks().get()));
                    p.minBuyNumber().set(Integer.valueOf(mp.Min().get()));
                    supplies.add(builder.newInstance());
                }
            }
            return supplies;
        }

        private static List<String> singleItemListOf(String item)
        {
            List<String> list;
            if (item == null)
            {
                list = Collections.emptyList();
            } else
            {
                list = List.of(item);
            }
            return list;
        }

        @SuppressWarnings("SameParameterValue")
        private Integer integerOf(Property<String> prop, int defaultValue)
        {
            if (prop.get() == null)
                return defaultValue;
            try
            {
                return Integer.valueOf(prop.get());
            } catch (NumberFormatException e)
            {
                System.err.println(spi.propertyDescriptorFor(prop).qualifiedName() + " is an invalid integer. Returning default value.");
                return defaultValue;
            }
        }

        private Integer reelSizeOf(MouserPart mp)
        {
            List<ProductAttribute> productAttributes = mp.ProductAttributes().get();
            return productAttributes.stream()
                .filter(pa -> pa.AttributeName().get().equals("Standard Pack Qty"))
                .map(ProductAttribute::AttributeValue).map(v -> Integer.parseInt(v.get()))
                .findAny().orElse(0);
        }

        private int computeWithinWeek(MouserPart mp)
        {
            int available = integerOf(mp.AvailabilityInStock(), 0);

            List<AvailabilityOnOrderObject> onOrder = mp.AvailabilityOnOrder().get();
            for (AvailabilityOnOrderObject order : onOrder)
            {
                LocalDateTime eightDaysFromNow = LocalDateTime.now().plus(8, ChronoUnit.DAYS);
                if (order.Date().get().isBefore(eightDaysFromNow))
                    available += order.Quantity().get();
            }
            return available;
        }

        private SortedSet<Price> getPriceList(List<Pricebreak> prices)
        {
            TreeSet<Price> result = new TreeSet<>(new Price.PriceComparator());
            ValueBuilder<Price> builder = vbf.newValueBuilder(Price.class);
            prices.forEach(p ->
            {
                Price prototype = builder.prototype();
                prototype.quantity().set(p.Quantity().get());
                prototype.price().set(priceOf(p));
                result.add(builder.newInstance());
            });
            return result;
        }

        private static BigDecimal priceOf(Pricebreak p)
        {
            String priceText = p.Price().get();
            while (priceText.length() > 0 && !priceText.matches("^[0-9.]*"))
                priceText = priceText.substring(1);
            if (priceText.length() > 0)
                return new BigDecimal(priceText);
            return new BigDecimal(Integer.MAX_VALUE);
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
                paths.put(MANUFACTURER_LIST, MANUFACTURER_LIST_PATH);
                instance.paths().set(paths);

                instance.website().set(WEBSITE);
                instance.bomColumns().get().add(NAME);
                instance.bomColumns().get().add("Mouser");
                instance.bomColumns().get().add("Mouser_PN");
                instance.bomColumns().get().add("MouserPN");
                instance.bomColumns().get().add("Mouser-PN");

                ApiKeyAuthentication authMethod = createAuthMethod(uow);
                instance.authentication().set(authMethod);
                instance.enabled().set(false);
                eb.newInstance();
            }
        }

        private static ApiKeyAuthentication createAuthMethod(UnitOfWork uow)
        {
            EntityBuilder<ApiKeyAuthentication> builder = uow.newEntityBuilder(ApiKeyAuthentication.class, AUTH_IDENTITY);
            builder.instance().apiKey().set("");
            return builder.newInstance();
        }
    }
}
