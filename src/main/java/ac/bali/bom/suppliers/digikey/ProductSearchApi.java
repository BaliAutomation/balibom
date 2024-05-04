package ac.bali.bom.suppliers.digikey;

import ac.bali.bom.suppliers.Supplier;
import ac.bali.bom.suppliers.digikey.model.CategoriesResponse;
import ac.bali.bom.suppliers.digikey.model.CategoryResponse;
import ac.bali.bom.suppliers.digikey.model.DigiReelPricing;
import ac.bali.bom.suppliers.digikey.model.KeywordRequest;
import ac.bali.bom.suppliers.digikey.model.KeywordResponse;
import ac.bali.bom.suppliers.digikey.model.ManufacturersResponse;
import ac.bali.bom.suppliers.digikey.model.MediaResponse;
import ac.bali.bom.suppliers.digikey.model.PackageTypeByQuantityResponse;
import ac.bali.bom.suppliers.digikey.model.ProductAssociationsResponse;
import ac.bali.bom.suppliers.digikey.model.ProductDetails;
import ac.bali.bom.suppliers.digikey.model.ProductSubstitutesResponse;
import ac.bali.bom.suppliers.digikey.model.RecommendedProductsResponse;
import ac.bali.bom.suppliers.oauth2.OAuth2Authentication;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.UndeclaredThrowableException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.polygene.api.injection.scope.Service;
import org.apache.polygene.api.injection.scope.Structure;
import org.apache.polygene.api.mixin.Mixins;
import org.apache.polygene.api.mixin.NoopMixin;
import org.apache.polygene.api.serialization.Serialization;
import org.apache.polygene.api.structure.Application;
import org.apache.polygene.api.structure.ModuleDescriptor;

@SuppressWarnings("unused")
@Mixins({ProductSearchApi.Mixin.class, NoopMixin.class})
public interface ProductSearchApi
{
    ProductAssociationsResponse associations(Supplier supplier, String productNumber);

    CategoriesResponse categories(Supplier supplier);

    CategoryResponse categoriesById(Supplier supplier, Integer categoryId);

    DigiReelPricing digiReelPricing(Supplier supplier, String productNumber, Integer requestedQuantity);

    KeywordResponse keywordSearch(Supplier supplier, KeywordRequest body);

    ManufacturersResponse manufacturers(Supplier supplier);

    MediaResponse media(Supplier supplier, String productNumber);

    PackageTypeByQuantityResponse packageTypeByQuantity(Supplier supplier, String productNumber, Integer requestedQuantity, String packagingPreference);

    ProductDetails productDetails(Supplier supplier, String productNumber);

    RecommendedProductsResponse recommendedProducts(Supplier supplier, String productNumber, Integer limit, String searchOptionList, Boolean excludeMarketPlaceProducts);

    ProductSubstitutesResponse substitutions(Supplier supplier, String productNumber, String includes);

    abstract class Mixin
        implements ProductSearchApi
    {
        @Service
        Serialization serialization;

        @Structure
        ModuleDescriptor module;

        @Structure
        Application application;

        @Override
        public KeywordResponse keywordSearch(Supplier supplier, KeywordRequest body)
        {
            String path = supplier.paths().get().get(DigikeySupplier.SEARCH);
            String resourcePath = createResourcePath(supplier, path);
            HttpPost request = new HttpPost(resourcePath);
            System.out.println(serialization.serialize(body));
            HttpEntity entity = new StringEntity(serialization.serialize(body), ContentType.APPLICATION_JSON);
            request.setEntity(entity);
            return makeRequest(supplier, request, KeywordResponse.class);
        }

        @Override
        public ProductDetails productDetails(Supplier supplier, String productNumber)
        {
            String path = supplier.paths().get().get(DigikeySupplier.PRODUCT_DETAILS);
            path = path.replace("${productNumber}", productNumber);
            String resourcePath = createResourcePath(supplier, path);
            System.out.println(resourcePath);
            return makeRequest(supplier, new HttpGet(resourcePath), ProductDetails.class);
        }


        private String createResourcePath(Supplier supplier, String path)
        {
            Map<String, String> urls = supplier.hosts().get();
            String host = urls.get(DigikeySupplier.HOST_DEV);
            if (application.mode() == Application.Mode.production)
            {
                host = urls.get(DigikeySupplier.HOST);
            }
            return host + path;
        }

        private <T> T makeRequest(Supplier supplier, HttpUriRequest request, Class<T> responseType)
        {
            OAuth2Authentication oauth2 = (OAuth2Authentication) supplier.authentication().get();
            if (oauth2 == null || !oauth2.isValid())
                return null;
            try (final CloseableHttpClient httpclient = HttpClients.createDefault())
            {
                request.addHeader("X-DIGIKEY-Client-Id", oauth2.loginClientId().get());
                request.addHeader("Authorization", "Bearer " + oauth2.loginAccessToken().get());
                request.addHeader("X-DIGIKEY-Locale-Language", "en");
                request.addHeader("X-DIGIKEY-Locale-Currency", "USD");
                System.out.println("Digikey Request: " + request);
                return httpclient.execute(request, r ->
                {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    r.getEntity().writeTo(baos);
                    baos.flush();
                    String response = baos.toString(StandardCharsets.UTF_8);
                    baos.close();
                    System.out.println("Digikey Response: " + response);
                    System.out.println("Digikey RateLimit Remaining: " + r.getFirstHeader("X-RateLimit-Remaining"));
                    System.out.println("Digikey BurstLimit Remaining: " + r.getFirstHeader("X-BurstLimit-Remaining"));
                    switch (r.getStatusLine().getStatusCode())
                    {
                        case 200:
                            return serialization.deserialize(module, responseType, response);
                        case 400, 404, 415:
                            throw new IOException("Digikey Error: " + r.getStatusLine());
                        case 429:
                            throw new IOException("Digikey RateLimit");
                        default:
                            System.err.println("Digikey Unhandled Response Code" + r.getStatusLine());
                            return null;
                    }
                });
            } catch (IOException e)
            {
                throw new UndeclaredThrowableException(e);
            }
        }
    }
}