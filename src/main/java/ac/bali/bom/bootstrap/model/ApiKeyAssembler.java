package ac.bali.bom.bootstrap.model;

import ac.bali.bom.supply.apikeyauth.ApiKeyAuthentication;
import org.apache.polygene.bootstrap.Assembler;
import org.apache.polygene.bootstrap.AssemblyException;
import org.apache.polygene.bootstrap.ModuleAssembly;

import static org.apache.polygene.api.common.Visibility.application;

public class ApiKeyAssembler
    implements Assembler
{

    @Override
    public void assemble(ModuleAssembly module) throws AssemblyException
    {
        module.entities(ApiKeyAuthentication.class).visibleIn(application);
    }
}
