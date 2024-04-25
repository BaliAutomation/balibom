package ac.bali.bom.suppliers.mouser;

import ac.bali.bom.suppliers.Supplier;
import ac.bali.bom.suppliers.mouser.model.MouserManufacturersNameRoot;
import ac.bali.bom.suppliers.mouser.model.SearchByKeywordMfrNameRequestRoot;
import ac.bali.bom.suppliers.mouser.model.SearchByPartMfrNameRequestRoot;
import ac.bali.bom.suppliers.mouser.model.SearchResponseRoot;
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
import org.apache.polygene.api.serialization.Serialization;
import org.apache.polygene.api.structure.ModuleDescriptor;

@Mixins({ProductSearchApi.Mixin.class})
public interface ProductSearchApi
{

    SearchResponseRoot searchByKeywordMfrName(Supplier supplier, SearchByKeywordMfrNameRequestRoot body);

    SearchResponseRoot searchByPartMfrName(Supplier supplier, SearchByPartMfrNameRequestRoot body);

    MouserManufacturersNameRoot mouserManufacturersName(Supplier supplier);

    abstract class Mixin
        implements ProductSearchApi
    {
        @Service
        Serialization serialization;

        @Structure
        ModuleDescriptor module;

        private String apiKey;

        @Override
        public SearchResponseRoot searchByKeywordMfrName(Supplier supplier, SearchByKeywordMfrNameRequestRoot body)
        {
            String path = "https://api.mouser.com/api/v2/search/keywordandmanufacturer?apiKey=" + supplier.loginAccessToken().get();
            HttpPost request = new HttpPost(path);
            System.out.println(serialization.serialize(body));
            HttpEntity entity = new StringEntity(serialization.serialize(body), ContentType.APPLICATION_JSON);
            request.setEntity(entity);
            return makeRequest(request, SearchResponseRoot.class);
        }

        @Override
        public SearchResponseRoot searchByPartMfrName(Supplier supplier, SearchByPartMfrNameRequestRoot body)
        {
            String path = "https://api.mouser.com/api/v2/search/partnumberandmanufacturer?apiKey=" + supplier.loginAccessToken().get();
            HttpPost request = new HttpPost(path);
            System.out.println(serialization.serialize(body));
            HttpEntity entity = new StringEntity(serialization.serialize(body), ContentType.APPLICATION_JSON);
            request.setEntity(entity);
            return makeRequest(request, SearchResponseRoot.class);
        }

        @Override
        public MouserManufacturersNameRoot mouserManufacturersName(Supplier supplier)
        {
            String path = "https://api.mouser.com/api/v2/search/manufacturerlist?apiKey=" + supplier.loginAccessToken().get();
            return makeRequest(new HttpGet(path), MouserManufacturersNameRoot.class);
        }

        private <T> T makeRequest(HttpUriRequest request, Class<T> responseType)
        {
            try (final CloseableHttpClient httpclient = HttpClients.createDefault())
            {
                System.out.println("Mouser Request: " + request);

                return httpclient.execute(request, r ->
                {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    r.getEntity().writeTo(baos);
                    baos.flush();
                    String response = baos.toString(StandardCharsets.UTF_8);
                    baos.close();
                    System.out.println("Mouser Response: " + response);
                    switch (r.getStatusLine().getStatusCode())
                    {
                        case 200:
                            return serialization.deserialize(module, responseType, response);
                        case 400, 415:
                            throw new IOException("Mouser Error: " + r.getStatusLine());
                        case 429:
                            throw new IOException("Mouser RateLimit");
                        default:
                            System.err.println("Mouser Unhandled Response Code" + r.getStatusLine());
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
