package ac.bali.bom.bootstrap.model;

import ac.bali.bom.suppliers.apikeyauth.ApiKeyAuthentication;
import ac.bali.bom.suppliers.oauth2.OAuth2Service;
import org.apache.polygene.bootstrap.Assembler;
import org.apache.polygene.bootstrap.AssemblyException;
import org.apache.polygene.bootstrap.ModuleAssembly;

import static org.apache.polygene.api.common.Visibility.application;
import static org.apache.polygene.api.common.Visibility.layer;

public class ApiKeyAssembler
    implements Assembler
{

    @Override
    public void assemble(ModuleAssembly module) throws AssemblyException
    {
        module.entities(ApiKeyAuthentication.class).visibleIn(application);
    }
}
