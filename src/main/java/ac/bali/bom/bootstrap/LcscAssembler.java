package ac.bali.bom.bootstrap;

import ac.bali.bom.suppliers.lcsc.LcscSupplier;
import ac.bali.bom.suppliers.lcsc.LcscParameter;
import ac.bali.bom.suppliers.lcsc.LcscPart;
import ac.bali.bom.suppliers.lcsc.LcscPartPrice;
import ac.bali.bom.suppliers.lcsc.LcscPartResponse;
import org.apache.polygene.bootstrap.Assembler;
import org.apache.polygene.bootstrap.AssemblyException;
import org.apache.polygene.bootstrap.ModuleAssembly;

public class LcscAssembler
        implements Assembler {

    @Override
    public void assemble(ModuleAssembly module) throws AssemblyException {
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
