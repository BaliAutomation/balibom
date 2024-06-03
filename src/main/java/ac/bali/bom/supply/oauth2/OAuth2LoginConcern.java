package ac.bali.bom.supply.oauth2;

import ac.bali.bom.supply.AuthenticationMethod;
import ac.bali.bom.supply.LoginRequired;
import ac.bali.bom.supply.Supplier;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import org.apache.polygene.api.common.AppliesTo;
import org.apache.polygene.api.concern.ConcernOf;
import org.apache.polygene.api.injection.scope.Service;
import org.apache.polygene.api.injection.scope.Structure;
import org.apache.polygene.api.injection.scope.This;
import org.apache.polygene.api.unitofwork.UnitOfWorkFactory;

@AppliesTo(LoginRequired.class)
public class OAuth2LoginConcern extends ConcernOf<InvocationHandler>
    implements InvocationHandler
{
    @This
    Supplier supplier;

    @Structure
    UnitOfWorkFactory uowf;

    @Service
    OAuth2Service oauth2;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
    {
        AuthenticationMethod authenticationMethod = supplier.authentication().get();
        if( authenticationMethod instanceof OAuth2Authentication oauth)
        {
            if( oauth.isValid() )
                login(oauth);
        }
        return next.invoke(proxy, method, args);
    }

    private void login(OAuth2Authentication auth)
    {
        try
        {
            Long expiry = auth.loginExpirationDateTime().get();
            if (expiry == null || expiry < System.currentTimeMillis() + 30000)
            {
                URL url = new URL(auth.loginEndpoint().get());
                OAuth2AccessToken token = oauth2.authorize(url, auth.loginClientId().get(), auth.loginClientSecret().get());
                auth.loginAccessToken().set(token.access_token().get());
                // refreshToken().set( oAuth2AccessTokenResponse.refresh_token().get() );
                // Response expiration time is in seconds, convert to milliseconds before saving.
                auth.loginExpirationDateTime().set(System.currentTimeMillis() + token.expires_in().get() * 1000);
            }
        } catch (MalformedURLException | AuthorizationException e)
        {
            throw new RuntimeException(e);
        }
    }
}
