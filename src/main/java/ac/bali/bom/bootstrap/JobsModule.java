package ac.bali.bom.bootstrap;

import ac.bali.bom.jobs.Job;
import ac.bali.bom.jobs.JobsService;
import org.apache.polygene.bootstrap.AssemblyException;
import org.apache.polygene.bootstrap.LayerAssembly;
import org.apache.polygene.bootstrap.ModuleAssembly;
import org.apache.polygene.bootstrap.layered.ModuleAssembler;

import static org.apache.polygene.api.common.Visibility.application;

public class JobsModule
    implements ModuleAssembler
{
    public static final String NAME = "Jobs Module";

    @Override
    public ModuleAssembly assemble(LayerAssembly layer, ModuleAssembly module) throws AssemblyException
    {
        module.defaultServices();
        module.entities(Job.class).visibleIn(application);
        module.values(Job.class).visibleIn(application);
        module.services(JobsService.class).visibleIn(application);
        return module;
    }
}
