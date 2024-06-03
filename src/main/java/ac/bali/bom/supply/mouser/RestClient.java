package ac.bali.bom.supply.mouser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.UndeclaredThrowableException;
import java.nio.charset.StandardCharsets;
import org.apache.polygene.api.structure.ModuleDescriptor;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.polygene.api.serialization.Serialization;

public class RestClient
{
    static <T> T makeRequest(HttpUriRequest request, Class<T> responseType, Serialization serialization, ModuleDescriptor module)
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

                //noinspection EnhancedSwitchMigration
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
