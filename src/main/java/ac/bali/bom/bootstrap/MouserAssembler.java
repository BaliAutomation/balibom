package ac.bali.bom.bootstrap;

import ac.bali.bom.suppliers.mouser.MouserSupplier;
import ac.bali.bom.suppliers.mouser.ProductSearchApi;
import ac.bali.bom.suppliers.mouser.model.AlternatePackaging;
import ac.bali.bom.suppliers.mouser.model.AvailabilityOnOrderObject;
import ac.bali.bom.suppliers.mouser.model.ErrorEntity;
import ac.bali.bom.suppliers.mouser.model.MouserManufacturerName;
import ac.bali.bom.suppliers.mouser.model.MouserManufacturerNameList;
import ac.bali.bom.suppliers.mouser.model.MouserManufacturersNameRoot;
import ac.bali.bom.suppliers.mouser.model.MouserPart;
import ac.bali.bom.suppliers.mouser.model.Pricebreak;
import ac.bali.bom.suppliers.mouser.model.ProductAttribute;
import ac.bali.bom.suppliers.mouser.model.ProductCompliance;
import ac.bali.bom.suppliers.mouser.model.SearchByKeywordMfrNameRequest;
import ac.bali.bom.suppliers.mouser.model.SearchByKeywordMfrNameRequestRoot;
import ac.bali.bom.suppliers.mouser.model.SearchByPartMfrNameRequest;
import ac.bali.bom.suppliers.mouser.model.SearchByPartMfrNameRequestRoot;
import ac.bali.bom.suppliers.mouser.model.SearchResponse;
import ac.bali.bom.suppliers.mouser.model.SearchResponseRoot;
import ac.bali.bom.suppliers.mouser.model.StandardCost;
import ac.bali.bom.suppliers.mouser.model.UnitWeightKg;
import org.apache.polygene.bootstrap.Assembler;
import org.apache.polygene.bootstrap.AssemblyException;
import org.apache.polygene.bootstrap.ModuleAssembly;

public class MouserAssembler
    implements Assembler
{

    @Override
    public void assemble(ModuleAssembly module)
        throws AssemblyException
    {
        module.values(
            MouserManufacturerNameList.class,
            ProductAttribute.class,
            MouserPart.class,
            SearchResponse.class,
            SearchByKeywordMfrNameRequestRoot.class,
            ErrorEntity.class,
            StandardCost.class,
            AlternatePackaging.class,
            MouserManufacturerName.class,
            SearchResponseRoot.class,
            SearchByPartMfrNameRequestRoot.class,
            Pricebreak.class,
            AvailabilityOnOrderObject.class,
            ProductCompliance.class,
            SearchByPartMfrNameRequest.class,
            MouserManufacturersNameRoot.class,
            SearchByKeywordMfrNameRequest.class,
            UnitWeightKg.class
        );

        module.addServices(ProductSearchApi .class)
            .instantiateOnStartup()
        ;

        module.addServices(MouserSupplier.class)
            .identifiedBy("mouser")
            .instantiateOnStartup()
        ;
    }
}
