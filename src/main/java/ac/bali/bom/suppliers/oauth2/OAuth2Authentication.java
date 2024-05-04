package ac.bali.bom.suppliers.oauth2;

import ac.bali.bom.suppliers.AuthenticationMethod;
import org.apache.polygene.api.common.Optional;
import org.apache.polygene.api.mixin.Mixins;
import org.apache.polygene.api.property.Property;

@Mixins(OAuth2Authentication.Mixin.class)
public interface OAuth2Authentication extends AuthenticationMethod
{
    Property<String> loginClientId();

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
