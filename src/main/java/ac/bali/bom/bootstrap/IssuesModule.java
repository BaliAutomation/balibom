package ac.bali.bom.bootstrap;

import ac.bali.bom.issues.Issue;
import ac.bali.bom.issues.IssuesService;
import org.apache.polygene.bootstrap.AssemblyException;
import org.apache.polygene.bootstrap.LayerAssembly;
import org.apache.polygene.bootstrap.ModuleAssembly;
import org.apache.polygene.bootstrap.layered.ModuleAssembler;

import static org.apache.polygene.api.common.Visibility.application;

public class IssuesModule
    implements ModuleAssembler
{
    public static final String NAME = "Issues Module";

    @Override
    public ModuleAssembly assemble(LayerAssembly layer, ModuleAssembly module) throws AssemblyException
    {
        module.defaultServices();
        module.entities(Issue.class);
        module.services(IssuesService.class).visibleIn(application);
        return module;
    }
}
