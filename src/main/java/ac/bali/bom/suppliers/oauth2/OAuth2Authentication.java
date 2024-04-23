package ac.bali.bom.suppliers.oauth2;

import org.apache.polygene.api.common.Optional;
import org.apache.polygene.api.common.UseDefaults;
import org.apache.polygene.api.property.Property;

public interface OAuth2Authentication
{
    @UseDefaults
    Property<Boolean> useOAuth2();

    @Optional
    Property<String> loginClientId();

    @Optional
    Property<String> loginClientSecret();

    @Optional
    Property<String> loginAccessToken();

    @Optional
    Property<String> loginRefreshToken();

    @Optional
    Property<Long> loginExpirationDateTime();

    @Optional
    Property<String> loginEndpoint();
}
