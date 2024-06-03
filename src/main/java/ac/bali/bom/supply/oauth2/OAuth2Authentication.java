package ac.bali.bom.supply.oauth2;

import ac.bali.bom.supply.AuthenticationMethod;
import org.apache.polygene.api.mixin.Mixins;
import org.apache.polygene.api.property.Property;
import org.qi4j.library.crudui.SecretField;

@Mixins(OAuth2Authentication.Mixin.class)
public interface OAuth2Authentication extends AuthenticationMethod
{
    Property<String> loginClientId();

    @SecretField
    Property<String> loginClientSecret();

    Property<String> loginAccessToken();

    Property<String> loginRefreshToken();

    Property<Long> loginExpirationDateTime();

    Property<String> loginEndpoint();

    abstract class Mixin
        implements OAuth2Authentication
    {
        @Override
        public boolean isValid()
        {
            String clientSecret = loginClientSecret().get();
            String clientId = loginClientId().get();
            return !clientId.isEmpty() && !clientSecret.isEmpty();
        }
    }
}
