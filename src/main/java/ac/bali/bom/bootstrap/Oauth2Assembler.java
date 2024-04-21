package ac.bali.bom.bootstrap;

import ac.bali.bom.suppliers.oauth2.OAuth2AccessToken;
import ac.bali.bom.suppliers.oauth2.OAuth2Authentication;
import ac.bali.bom.suppliers.oauth2.OAuth2Service;
import org.apache.polygene.bootstrap.Assembler;
import org.apache.polygene.bootstrap.AssemblyException;
import org.apache.polygene.bootstrap.ModuleAssembly;

import static org.apache.polygene.api.common.Visibility.layer;

public class Oauth2Assembler
        implements Assembler {

    @Override
    public void assemble(ModuleAssembly module) throws AssemblyException {
        module.transients(OAuth2Authentication.class).visibleIn(layer);
        module.values(OAuth2AccessToken.class).visibleIn(layer);

        module.services(OAuth2Service.class)
            .identifiedBy("oauth2")
            .visibleIn(layer)
        ;
    }
}
