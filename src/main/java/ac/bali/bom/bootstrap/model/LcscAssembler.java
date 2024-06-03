package ac.bali.bom.bootstrap.model;

import ac.bali.bom.supply.lcsc.LcscParameter;
import ac.bali.bom.supply.lcsc.LcscPart;
import ac.bali.bom.supply.lcsc.LcscPartPrice;
import ac.bali.bom.supply.lcsc.LcscPartResponse;
import ac.bali.bom.supply.lcsc.LcscSupplier;
import org.apache.polygene.bootstrap.Assembler;
import org.apache.polygene.bootstrap.AssemblyException;
import org.apache.polygene.bootstrap.ModuleAssembly;

public class LcscAssembler
    implements Assembler
{

    @Override
    public void assemble(ModuleAssembly module) throws AssemblyException
    {
        module.values(
            LcscParameter.class,
            LcscPart.class,
            LcscPartPrice.class,
            LcscPartResponse.class
        );

        module.addServices(LcscSupplier.class)
            .identifiedBy("lcsc")
            .instantiateOnStartup()
        ;
    }
}
