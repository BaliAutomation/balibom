package ac.bali.bom.supply;

import ac.bali.bom.manufacturers.Manufacturer;
import ac.bali.bom.supply.digikey.DigikeySupplier;
import ac.bali.bom.supply.lcsc.LcscSupplier;
import ac.bali.bom.supply.manual.ManualSupplier;
import ac.bali.bom.supply.mouser.MouserSupplier;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.polygene.api.concern.Concerns;
import org.apache.polygene.api.injection.scope.Structure;
import org.apache.polygene.api.mixin.Mixins;
import org.apache.polygene.api.query.Query;
import org.apache.polygene.api.query.QueryBuilder;
import org.apache.polygene.api.query.QueryBuilderFactory;
import org.apache.polygene.api.service.ServiceActivation;
import org.apache.polygene.api.structure.Application;
import org.apache.polygene.api.unitofwork.UnitOfWork;
import org.apache.polygene.api.unitofwork.UnitOfWorkFactory;
import org.apache.polygene.api.unitofwork.concern.UnitOfWorkConcern;
import org.apache.polygene.api.unitofwork.concern.UnitOfWorkPropagation;
import org.apache.polygene.api.usecase.UsecaseBuilder;
import org.apache.polygene.api.value.ValueBuilderFactory;
import org.qi4j.library.crudui.Action;

import static org.apache.polygene.api.unitofwork.concern.UnitOfWorkPropagation.Propagation.MANDATORY;
import static org.qi4j.library.crudui.ActionScope.type;

@Mixins(SuppliersService.Mixin.class)
@Concerns(UnitOfWorkConcern.class)
public interface SuppliersService extends ServiceActivation
{
    @Action(label = "Search MF/MPN...", scope = type)
    @UnitOfWorkPropagation(MANDATORY)
    List<Supply> searchSupply(Manufacturer mf, String mpn);

    @Action(label = "Search MPN...", scope = type)
    @UnitOfWorkPropagation(MANDATORY)
    List<Supply> searchSupply(String mpn);

    @UnitOfWorkPropagation(MANDATORY)
    List<Supplier> suppliers();

    Supply findSupply(String supplier, String supplierPartNumber, Map<String,String> attributes);

    Supply findSupply(String supplierName, Manufacturer mf, String mpn, Map<String,String> attributes);

    class Mixin
        implements SuppliersService
    {
        @Structure
        Application application;

        @Structure
        QueryBuilderFactory qbf;

        @Structure
        UnitOfWorkFactory uowf;

        @Structure
        ValueBuilderFactory vbf;

        @Override
        public List<Supply> searchSupply(Manufacturer mf, String mpn)
        {
            return suppliers().stream()
                .filter(Objects::nonNull)
                .map( supplier -> supplier.searchManufacturerPartNumber(mf, mpn, Collections.emptyMap()))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        }

        @Override
        public List<Supply> searchSupply(String mpn)
        {
            return suppliers().stream()
                .filter(Objects::nonNull)
                .flatMap( supplier -> supplier.searchKeywords(mpn).stream())
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        }

        @Override
        public Supply findSupply(String supplierName, Manufacturer mf, String mpn, Map<String,String> attributes)
        {
            Supplier supplier = supplierNamed(supplierName);
            return supplier.searchManufacturerPartNumber(mf, mpn, attributes);
        }

        @Override
        public Supply findSupply(String supplierName, String supplierPartNumber, Map<String,String> attributes)
        {
            return supplierNamed(supplierName).searchSupplierPartNumber(supplierPartNumber, attributes);
        }

        @Override
        public List<Supplier> suppliers()
        {
            //noinspection resource
            UnitOfWork uow = uowf.currentUnitOfWork();
            QueryBuilder<Supplier> builder = qbf.newQueryBuilder(Supplier.class);
            Query<Supplier> supplies = uow.newQuery(builder);
            return supplies.stream().toList();
        }

        private Supplier supplierNamed(String supplierName)
        {
            return suppliers().stream().filter(s -> supplierName.equals(s.name().get())).findAny().orElse(null);
        }

        @Override
        public void activateService() throws Exception
        {
            try(UnitOfWork uow = uowf.newUnitOfWork(UsecaseBuilder.newUsecase("Load/Create Suppliers")))
            {
                DigikeySupplier.Mixin.createSupplier(uow, application.mode());
                LcscSupplier.Mixin.createSupplier(uow);
                MouserSupplier.Mixin.createSupplier(uow);
                ManualSupplier.Mixin.createSupplier(uow);
                uow.complete();
            }
        }

        @Override
        public void passivateService() throws Exception
        {

        }
    }
}
