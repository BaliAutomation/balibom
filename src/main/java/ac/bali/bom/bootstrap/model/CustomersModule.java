package ac.bali.bom.bootstrap.model;

import ac.bali.bom.customers.Customer;
import ac.bali.bom.customers.CustomerService;
import org.apache.polygene.bootstrap.AssemblyException;
import org.apache.polygene.bootstrap.LayerAssembly;
import org.apache.polygene.bootstrap.ModuleAssembly;
import org.apache.polygene.bootstrap.layered.ModuleAssembler;

import static org.apache.polygene.api.common.Visibility.application;

public class CustomersModule
    implements ModuleAssembler
{
    public static final String NAME = "Jobs Module";

    @Override
    public ModuleAssembly assemble(LayerAssembly layer, ModuleAssembly module) throws AssemblyException
    {
        module.defaultServices();
        module.entities(Customer.class).visibleIn(application);
        module.services(CustomerService.class).visibleIn(application);
        return module;
    }
}
