package ac.bali.bom.suppliers.digikey;

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

@Mixins({ProductSearchApi.Mixin.class, NoopMixin.class})
public interface ProductSearchApi
{
    void updateAuthMethod(OAuth2Authentication authenticationMethod);

    ProductAssociationsResponse associations(String productNumber);

    CategoriesResponse categories();

    CategoryResponse categoriesById(Integer categoryId);

    DigiReelPricing digiReelPricing(String productNumber, Integer requestedQuantity);

    KeywordResponse keywordSearch(KeywordRequest body);

    ManufacturersResponse manufacturers();

    MediaResponse media(String productNumber);

    PackageTypeByQuantityResponse packageTypeByQuantity(String productNumber, Integer requestedQuantity, String packagingPreference);

    ProductDetails productDetails(String productNumber);

    RecommendedProductsResponse recommendedProducts(String productNumber, Integer limit, String searchOptionList, Boolean excludeMarketPlaceProducts);

    ProductSubstitutesResponse substitutions(String productNumber, String includes);

    abstract class Mixin
        implements ProductSearchApi
    {
        @Service
        Serialization serialization;

        @Structure
        ModuleDescriptor module;

        @Structure
        Application application;

        private OAuth2Authentication authMethod;

        @Override
        public void updateAuthMethod(OAuth2Authentication authenticationMethod)
        {
            this.authMethod = authenticationMethod;
        }

        @Override
        public KeywordResponse keywordSearch(KeywordRequest body)
        {
            String path = "/products/v4/search/keyword";
            String resourcePath = createResourcePath(path);
            HttpPost request = new HttpPost(resourcePath);
            System.out.println(serialization.serialize(body));
            HttpEntity entity = new StringEntity(serialization.serialize(body), ContentType.APPLICATION_JSON);
            request.setEntity(entity);
            return makeRequest(request, KeywordResponse.class);
        }

        @Override
        public ProductDetails productDetails(String productNumber)
        {
            String path = "/products/v4/search/" + productNumber + "/productdetails";
            String resourcePath = createResourcePath(path);
            return makeRequest(new HttpGet(resourcePath), ProductDetails.class);
        }

        private String createResourcePath(String path)
        {
            String host = "https://sandbox-api.digikey.com";
            if (application.mode() == Application.Mode.production)
            {
                host = "https://api.digikey.com";
            }
            return host + path;
        }

        private <T> T makeRequest(HttpUriRequest request, Class<T> responseType)
        {
            try (final CloseableHttpClient httpclient = HttpClients.createDefault())
            {
                request.addHeader("X-DIGIKEY-Client-Id", authMethod.loginClientId().get());
                request.addHeader("Authorization", "Bearer " + authMethod.loginAccessToken().get());
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
                    switch( r.getStatusLine().getStatusCode())
                    {
                        case 200:
                            return serialization.deserialize(module, responseType, response);
                        case 400, 415:
                            throw new IOException("Digikey Error: " + r.getStatusLine());
                        case 429:
                            throw new IOException("Digikey RateLimit");
                        default:
                            System.err.println("Digikey Unhandled Response Code" + r.getStatusLine() );
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