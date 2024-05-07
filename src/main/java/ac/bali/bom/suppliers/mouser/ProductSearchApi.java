package ac.bali.bom.suppliers.mouser;

import ac.bali.bom.suppliers.Supplier;
import ac.bali.bom.suppliers.apikeyauth.ApiKeyAuthentication;
import ac.bali.bom.suppliers.mouser.model.SearchByKeywordMfrNameRequestRoot;
import ac.bali.bom.suppliers.mouser.model.SearchByPartMfrNameRequestRoot;
import ac.bali.bom.suppliers.mouser.model.SearchResponseRoot;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
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


    abstract class Mixin
        implements ProductSearchApi
    {
        @Service
        Serialization serialization;

        @Structure
        ModuleDescriptor module;

        @Override
        public SearchResponseRoot searchByKeywordMfrName(Supplier supplier, SearchByKeywordMfrNameRequestRoot body)
        {
            return sendToApi(supplier, MouserSupplier.SEARCH, serialization.serialize(body));
        }

        @Override
        public SearchResponseRoot searchByPartMfrName(Supplier supplier, SearchByPartMfrNameRequestRoot body)
        {
            return sendToApi(supplier, MouserSupplier.PRODUCTS, serialization.serialize(body));
        }

        private SearchResponseRoot sendToApi(Supplier supplier, String pathEntry, String body)
        {
            String host = supplier.hosts().get().get(MouserSupplier.HOST);
            String path = supplier.paths().get().get(pathEntry);
            ApiKeyAuthentication authenticationMethod = (ApiKeyAuthentication) supplier.authentication().get();
            path = path.replace("${apiKey}", authenticationMethod.apiKey().get());
            HttpPost request = new HttpPost(host + path);
            System.out.println(body);
            HttpEntity entity = new StringEntity(body, ContentType.APPLICATION_JSON);
            request.setEntity(entity);
            return RestClient.makeRequest(request, SearchResponseRoot.class,serialization, module);
        }


    }
}
