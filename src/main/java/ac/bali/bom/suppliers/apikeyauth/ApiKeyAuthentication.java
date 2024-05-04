package ac.bali.bom.suppliers.apikeyauth;

import ac.bali.bom.suppliers.AuthenticationMethod;
import org.apache.polygene.api.mixin.Mixins;
import org.apache.polygene.api.property.Property;

@Mixins(ApiKeyAuthentication.Mixin.class)
public interface ApiKeyAuthentication extends AuthenticationMethod
{
    Property<String> apiKey();

    abstract class Mixin
        implements ApiKeyAuthentication
    {
        @Override
        public boolean isValid()
        {
            String apiKey = apiKey().get();
            return !apiKey.trim().isEmpty();
        }
    }
}
