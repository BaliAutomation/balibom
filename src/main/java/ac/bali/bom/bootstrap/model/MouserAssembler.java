package ac.bali.bom.bootstrap.model;

import ac.bali.bom.supply.mouser.MouserSupplier;
import ac.bali.bom.supply.mouser.ProductSearchApi;
import ac.bali.bom.supply.mouser.model.AlternatePackaging;
import ac.bali.bom.supply.mouser.model.AvailabilityOnOrderObject;
import ac.bali.bom.supply.mouser.model.ErrorEntity;
import ac.bali.bom.supply.mouser.model.MouserManufacturerName;
import ac.bali.bom.supply.mouser.model.MouserManufacturerNameList;
import ac.bali.bom.supply.mouser.model.MouserManufacturersNameRoot;
import ac.bali.bom.supply.mouser.model.MouserPart;
import ac.bali.bom.supply.mouser.model.Pricebreak;
import ac.bali.bom.supply.mouser.model.ProductAttribute;
import ac.bali.bom.supply.mouser.model.ProductCompliance;
import ac.bali.bom.supply.mouser.model.SearchByKeywordMfrNameRequest;
import ac.bali.bom.supply.mouser.model.SearchByKeywordMfrNameRequestRoot;
import ac.bali.bom.supply.mouser.model.SearchByPartMfrNameRequest;
import ac.bali.bom.supply.mouser.model.SearchByPartMfrNameRequestRoot;
import ac.bali.bom.supply.mouser.model.SearchResponse;
import ac.bali.bom.supply.mouser.model.SearchResponseRoot;
import ac.bali.bom.supply.mouser.model.StandardCost;
import ac.bali.bom.supply.mouser.model.UnitWeightKg;
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

        module.addServices(ProductSearchApi.class)
            .instantiateOnStartup()
        ;

        module.addServices(MouserSupplier.class)
            .identifiedBy("mouser")
            .instantiateOnStartup()
        ;
    }
}
