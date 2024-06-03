package ac.bali.bom.supply.oauth2;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import org.apache.polygene.api.injection.scope.Service;
import org.apache.polygene.api.injection.scope.Structure;
import org.apache.polygene.api.mixin.Mixins;
import org.apache.polygene.api.serialization.Serialization;
import org.apache.polygene.api.structure.ModuleDescriptor;

@Mixins(OAuth2Service.Mixin.class)
public interface OAuth2Service
{
    OAuth2AccessToken authorize(URL postUrl, String clientId, String clientSecret)
        throws AuthorizationException;

    class Mixin
        implements OAuth2Service
    {
        @Structure
        ModuleDescriptor module;

        @Service
        Serialization serializer;

        public OAuth2AccessToken authorize(URL postUrl, String clientId, String clientSecret)
            throws AuthorizationException
        {
            String body = "client_id=" + clientId + "&client_secret=" + clientSecret + "&grant_type=client_credentials";
            System.out.println("Authorization body: " + body);

            URLConnection con;
            try
            {
                con = postUrl.openConnection();
            } catch (IOException e)
            {
                System.err.println("Unable to open connection with OAuth server. " + e.getMessage());
                throw new AuthorizationException("Unable to retrieve OAuth token. Error with connection to OAuth server. " + e.getMessage());
            }
            HttpURLConnection http = (HttpURLConnection) con;
            http.setConnectTimeout(30000);
            try
            {
                http.setRequestMethod("POST");
            } catch (ProtocolException e)
            {
                // This will only happen if you type something that isn't POST above.
                System.err.println("Invalid HTTP verb. " + e.getMessage());
                throw new AuthorizationException("Unable to retrieve OAuth token. HTTP Verb was not POST.");
            }
            http.setDoOutput(true);
            http.setDoInput(true);
            http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            byte[] postData = body.getBytes(StandardCharsets.UTF_8);
            http.setRequestProperty("Content-Length", Integer.toString(postData.length));
            StringBuilder content = new StringBuilder();
            try (DataOutputStream stream = new DataOutputStream(http.getOutputStream()))
            {
                stream.write(postData);

                if (http.getResponseCode() == 401 || http.getResponseCode() == 400)
                    System.err.println("Client Id + Secret + Redirect combo was invalid when attempting to get token. Or if refreshing, refresh token may be expired.");
                BufferedReader in = new BufferedReader(new InputStreamReader(http.getInputStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null)
                {
                    content.append(inputLine);
                }
                in.close();
            } catch (IOException e)
            {
                System.err.println("Unable to send OAuth request. " + e.getMessage());
                throw new AuthorizationException("Unable to retrieve OAuth token. Connection Error. " + e.getMessage(), e);
            }
            try
            {
                System.out.println(content);
                return serializer.deserialize(module, OAuth2AccessToken.class, content.toString());
            } finally
            {
                http.disconnect();
            }
        }
    }
}