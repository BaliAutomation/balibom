package ac.bali.bom.bootstrap.model;

import ac.bali.bom.supply.manual.ManualSupplier;
import ac.bali.bom.supply.manual.model.Product;
import ac.bali.bom.view.ManualProductSearch;
import org.apache.polygene.bootstrap.Assembler;
import org.apache.polygene.bootstrap.AssemblyException;
import org.apache.polygene.bootstrap.ModuleAssembly;

public class ManualSupplierAssembler
    implements Assembler
{

    @Override
    public void assemble(ModuleAssembly module) throws AssemblyException
    {
        module.values(
            Product.class
        );

        module.addServices(ManualSupplier.class)
            .identifiedBy("manually")
            .instantiateOnStartup()
        ;

        module.addServices(ManualProductSearch.class)
            .identifiedBy("manual-provider")
            .instantiateOnStartup()
        ;
    }
}
