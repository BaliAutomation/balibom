package ac.bali.bom.suppliers.oauth2;

import org.apache.polygene.api.common.Optional;
import org.apache.polygene.api.common.UseDefaults;
import org.apache.polygene.api.mixin.Mixins;
import org.apache.polygene.api.property.Property;

public interface OAuth2Authentication
{
    @UseDefaults("<not set>")
    Property<String> loginClientId();

    @UseDefaults("<not set>")
    Property<String> loginClientSecret();

    @Optional
    Property<String> loginAccessToken();

    @Optional
    Property<String> loginRefreshToken();

    @Optional
    Property<Long> loginExpirationDateTime();

    Property<String> loginEndpoint();
}
