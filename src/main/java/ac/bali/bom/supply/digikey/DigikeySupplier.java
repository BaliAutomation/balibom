package ac.bali.bom.supply.digikey;

import ac.bali.bom.manufacturers.Manufacturer;
import ac.bali.bom.manufacturers.ManufacturersService;
import ac.bali.bom.parts.Price;
import ac.bali.bom.supply.Supplier;
import ac.bali.bom.supply.SupplierProvider;
import ac.bali.bom.supply.Supply;
import ac.bali.bom.supply.digikey.model.FilterOptionsRequest;
import ac.bali.bom.supply.digikey.model.KeywordRequest;
import ac.bali.bom.supply.digikey.model.KeywordResponse;
import ac.bali.bom.supply.digikey.model.ParameterValue;
import ac.bali.bom.supply.digikey.model.PriceBreak;
import ac.bali.bom.supply.digikey.model.Product;
import ac.bali.bom.supply.digikey.model.ProductDetails;
import ac.bali.bom.supply.digikey.model.ProductVariation;
import ac.bali.bom.supply.digikey.model.SortOptions;
import ac.bali.bom.supply.oauth2.OAuth2Authentication;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import org.apache.polygene.api.entity.EntityBuilder;
import org.apache.polygene.api.identity.Identity;
import org.apache.polygene.api.identity.StringIdentity;
import org.apache.polygene.api.injection.scope.Service;
import org.apache.polygene.api.injection.scope.Structure;
import org.apache.polygene.api.mixin.Mixins;
import org.apache.polygene.api.structure.Application;
import org.apache.polygene.api.unitofwork.NoSuchEntityException;
import org.apache.polygene.api.unitofwork.UnitOfWork;
import org.apache.polygene.api.unitofwork.UnitOfWorkFactory;
import org.apache.polygene.api.value.ValueBuilder;
import org.apache.polygene.api.value.ValueBuilderFactory;

@Mixins(DigikeySupplier.Mixin.class)
public interface DigikeySupplier extends SupplierProvider
{

    String HOSTNAME = "https://api.digikey.com";
    String HOST = "production";
    String HOSTNAME_DEV = "https://sandbox-api.digikey.com";
    String HOST_DEV = "sandbox";

    String PRODUCT_DETAILS = "productDetailsApi";
    String PRODUCT_DETAILS_PATH = "/products/v4/search/${productNumber}/productdetails";

    String ORDERING = "orderingApi";
    String ORDERING_PATH = "/orderstatus/v4/orders";

    String SEARCH = "searchApi";
    String SEARCH_PATH = "/products/v4/search/keyword";

    String LOGIN_ENDPOINT_PATH = "/v1/oauth2/token";
    String WEBSITE_PATH = "https://digikey.com";


    class Mixin
        implements DigikeySupplier
    {
        private static final String NAME = "DigiKey";
        public static final Identity IDENTITY = StringIdentity.identityOf("supplier/DigiKey");
        public static final Identity AUTH_IDENTITY = StringIdentity.identityOf("authentication/DigiKey");


        @Structure
        UnitOfWorkFactory uowf;

        @Structure
        ValueBuilderFactory vbf;

        @Service
        ProductSearchApi products;

        @Service
        ManufacturersService manufacturers;

        @Override
        public Supply searchSupplierPartNumber(Supplier supplier, String supplierPartNumber, Map<String, String> attributes)
        {
            ProductDetails prodDetails = products.productDetails(supplier, supplierPartNumber);
            if (prodDetails == null)
                return null;
            Product product = prodDetails.Product().get();
            return createSupply(supplier, product);
        }

        @Override
        public Supply searchManufacturerPartNumber(Supplier supplier, String mf, String mpn, Map<String, String> attributes)
        {
            if (mf == null || mf.trim().length() == 0)
            {
                return null;
            }
            if (mpn == null || mpn.trim().length() == 0)
            {
                return null;
            }
            SortOptions sortOptions = vbf.newValue(SortOptions.class);

            FilterOptionsRequest filterOptions = vbf.newValue(FilterOptionsRequest.class);

            ValueBuilder<KeywordRequest> builder3 = vbf.newValueBuilder(KeywordRequest.class);
            KeywordRequest prototype3 = builder3.prototype();
            prototype3.Keywords().set(mf + " " + mpn);
            prototype3.Offset().set(0);
            prototype3.Limit().set(10);
            prototype3.SortOptions().set(sortOptions);
            prototype3.FilterOptionsRequest().set(filterOptions);
            KeywordRequest search = builder3.newInstance();
            KeywordResponse response = products.keywordSearch(supplier, search);
            if (response == null)
            {
                System.err.println();
                return null;
            }
            Integer count = response.ProductsCount().get();
            List<Product> exactMatches = response.ExactMatches().get();
            List<Product> products = response.Products().get();
            if (count > 1)
            {
                Product product = findExact(mpn, products);
                if (product == null)
                {
                    System.err.println("WARNING: Digikey: More than one search result for " + mf + " " + mpn + ", but unable to identify a correct one.");
                    return null;
                }
                return createSupply(supplier, product);
            }
            if (exactMatches.size() > 0)
            {
                Product product = exactMatches.get(0);
                return createSupply(supplier, product);
            }
            if (count == 1)
            {
                Product product = response.Products().get().get(0);
                return createSupply(supplier, product);
            }
            return null;
        }

        private Product findExact(String mpn, List<Product> products)
        {
            for (Product p : products)
            {
                if (p.ManufacturerProductNumber().get().equals(mpn))
                    return p;
            }
            return null;
        }

        @Override
        public List<Supply> searchKeywords(Supplier supplier, String keywords)
        {
            return null;
        }

        private Supply createSupply(Supplier supplier, Product product)
        {
            ValueBuilder<Supply> builder = vbf.newValueBuilder(Supply.class);
            Supply p = builder.prototype();
            p.updatedOn().set(LocalDate.now());
            Manufacturer mf = manufacturers.findManufacturer(product.Manufacturer().get().Name().get());
            p.mf().set(mf);
            p.mpn().set(product.ManufacturerProductNumber().get());
            p.supplier().set(supplier);
            p.supplierPartNumber().set(product.ProductVariations().get().get(0).DigiKeyProductNumber().get());
            p.productIntro().set(product.Description().get().ProductDescription().get());
            p.availableSupply().set(product.QuantityAvailable().get().intValue());
            p.inStock().set(product.QuantityAvailable().get().intValue());
            p.canShipWithInWeek().set(product.QuantityAvailable().get().intValue());
            List<ProductVariation> variations = product.ProductVariations().get();
            p.reelSize().set(variations.get(0).StandardPackage().get());
            p.isReel().set(isReel(product));
            p.images().set(List.of(product.PhotoUrl().get()));
            p.datasheet().set(product.DatasheetUrl().get());
            p.parameters().set(getParameters(product));
            p.prices().set(getPriceList(product));
            p.minBuyNumber().set(minBuyNumber(product));
            return builder.newInstance();
        }

        private static Map<String, String> getParameters(Product product)
        {
            Map<String, String> result = new HashMap<>();
            for (ParameterValue p : product.Parameters().get())
            {
                String k = p.ParameterText().get();
                String v = p.ValueText().get();
                result.put(k, v);
            }
            return result;
        }

        private SortedSet<Price> getPriceList(Product product)
        {
            TreeSet<Price> result = new TreeSet<>(new Price.PriceComparator());
            ValueBuilder<Price> builder = vbf.newValueBuilder(Price.class);
            List<Price> prices = product.ProductVariations().get().stream()
                .filter( pv -> pv.PackageType().get().Id().get() != 243)                // Don't support Digi-Reel for now.
                .flatMap(pv -> pv.StandardPricing().get().stream())
                .map(pb -> createPrice(builder, pb))
                .toList();
            result.addAll(prices);
            return result;
        }

        private static Price createPrice(ValueBuilder<Price> builder, PriceBreak p)
        {
            Price prototype = builder.prototype();
            prototype.quantity().set(p.BreakQuantity().get());
            prototype.price().set(p.UnitPrice().get());
            return builder.newInstance();
        }

        private Boolean isReel(Product product)
        {
            for (ProductVariation pv : product.ProductVariations().get())
            {
                if (pv.PackageType().get().Id().get() == 1)
                    return true;
            }
            return false;
        }

        private Integer minBuyNumber(Product product)
        {
            int minQuantity = Integer.MAX_VALUE;
            for (ProductVariation pv : product.ProductVariations().get())
            {
                int minOrder = pv.MinimumOrderQuantity().get();
                if (minOrder < minQuantity)
                    minQuantity = minOrder;
            }
            return minQuantity;
        }

        public static void createSupplier(UnitOfWork uow, Application.Mode mode) throws Exception
        {
            Supplier supplier;
            try
            {
                supplier = uow.get(Supplier.class, IDENTITY);
                setLoginEndpoint(supplier, uow, mode);
            } catch (NoSuchEntityException e)
            {
                EntityBuilder<Supplier> eb = uow.newEntityBuilder(Supplier.class, IDENTITY);
                Supplier instance = eb.instance();
                instance.name().set("DigiKey");

                Map<String, String> hosts = new HashMap<>();
                hosts.put(HOST_DEV, HOSTNAME_DEV);
                hosts.put(HOST, HOSTNAME);
                instance.hosts().set(hosts);

                Map<String, String> paths = new HashMap<>();
                paths.put(PRODUCT_DETAILS, PRODUCT_DETAILS_PATH);
                paths.put(ORDERING, ORDERING_PATH);
                paths.put(SEARCH, SEARCH_PATH);
                instance.paths().set(paths);

                instance.website().set(WEBSITE_PATH);

                OAuth2Authentication authMethod = createAuthMethod(uow);
                instance.authentication().set(authMethod);
                instance.enabled().set(false);
                setLoginEndpoint(instance, uow, mode);
                supplier = eb.newInstance();
            }
            Set<String> newBomColumns = new HashSet<>(supplier.bomColumns().get());
            newBomColumns.add(NAME);
            newBomColumns.add("DigiKey_PN");
            newBomColumns.add("DigiKeyPN");
            newBomColumns.add("Digi-Key");
            newBomColumns.add("Digi-Key_PN");
            newBomColumns.add("Digi-KeyPN");
            supplier.bomColumns().set(newBomColumns);   // trigger the update if any. Setting inner content doesn't mark a property invalid, nor when the same value/instance is set again.
        }

        private static void setLoginEndpoint(Supplier supplier, UnitOfWork uow, Application.Mode mode)
        {
            OAuth2Authentication authentication = (OAuth2Authentication) supplier.authentication().get();
            String loginEndpoint = HOSTNAME_DEV + LOGIN_ENDPOINT_PATH;
            if (mode == Application.Mode.production)
            {
                loginEndpoint = HOSTNAME + LOGIN_ENDPOINT_PATH;
            }
            authentication.loginEndpoint().set(loginEndpoint);
        }

        private static OAuth2Authentication createAuthMethod(UnitOfWork uow)
        {
            EntityBuilder<OAuth2Authentication> builder = uow.newEntityBuilder(OAuth2Authentication.class, AUTH_IDENTITY);
            OAuth2Authentication proto = builder.instance();
            proto.loginClientId().set("");
            proto.loginClientSecret().set("");
            proto.loginEndpoint().set(LOGIN_ENDPOINT_PATH);
            proto.loginRefreshToken().set("");
            proto.loginAccessToken().set("");
            proto.loginExpirationDateTime().set(0L);
            return builder.newInstance();
        }
    }
}