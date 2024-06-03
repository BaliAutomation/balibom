package ac.bali.bom.supply.mouser;

import ac.bali.bom.supply.Supplier;
import ac.bali.bom.supply.apikeyauth.ApiKeyAuthentication;
import ac.bali.bom.supply.mouser.model.MouserManufacturerNameList;
import org.apache.http.client.methods.HttpGet;
import org.apache.polygene.api.injection.scope.Service;
import org.apache.polygene.api.injection.scope.Structure;
import org.apache.polygene.api.mixin.Mixins;
import org.apache.polygene.api.serialization.Serialization;
import org.apache.polygene.api.structure.ModuleDescriptor;

@Mixins(ManufacturerListApi.Mixin.class)
public interface ManufacturerListApi
{
    MouserManufacturerNameList fetchManufacturerList(Supplier supplier);

    class Mixin
    implements ManufacturerListApi{

        @Structure
        ModuleDescriptor module;

        @Service
        Serialization serialization;

        @Override
        public MouserManufacturerNameList fetchManufacturerList(Supplier supplier)
        {
            String host = supplier.hosts().get().get(MouserSupplier.HOST);
            String path = supplier.paths().get().get(MouserSupplier.MANUFACTURER_LIST);
            ApiKeyAuthentication authenticationMethod = (ApiKeyAuthentication) supplier.authentication().get();
            path = path.replace("${apiKey}", authenticationMethod.apiKey().get());
            HttpGet request = new HttpGet(host + path);
            return RestClient.makeRequest(request, MouserManufacturerNameList.class, serialization, module);
        }
    }
}
