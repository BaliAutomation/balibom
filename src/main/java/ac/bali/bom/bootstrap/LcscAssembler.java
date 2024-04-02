package ac.bali.bom.bootstrap;

import ac.bali.bom.connectivity.PartSupplyConnector;
import ac.bali.bom.lcsc.*;
import org.apache.polygene.api.common.Visibility;
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
                        LcscResponse.class
                )
                .visibleIn(Visibility.module);

        module.services(PartSupplyConnector.class)
                .withMixins(Lcsc.class)
                .identifiedBy("lcsc-connection")
                .instantiateOnStartup()
                .visibleIn(Visibility.application)
        ;
    }
}
