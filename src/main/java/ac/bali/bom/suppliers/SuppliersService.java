package ac.bali.bom.suppliers;

import ac.bali.bom.manufacturers.Manufacturer;
import ac.bali.bom.suppliers.digikey.DigikeySupplier;
import ac.bali.bom.suppliers.lcsc.LcscSupplier;
import ac.bali.bom.suppliers.mouser.MouserSupplier;
import java.util.List;
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
import org.qi4j.library.javafx.support.Action;

import static org.apache.polygene.api.unitofwork.concern.UnitOfWorkPropagation.Propagation.MANDATORY;
import static org.qi4j.library.javafx.support.ActionScope.type;

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

    Supply findSupply(String supplier, String supplierPartNumber);

    Supply findSupply(String supplierName, Manufacturer mf, String mpn);

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
                .map( supplier -> supplier.searchManufacturerPartNumber(mf, mpn))
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
        public Supply findSupply(String supplierName, Manufacturer mf, String mpn)
        {
            Supplier supplier = supplierNamed(supplierName);
            return supplier.searchManufacturerPartNumber(mf, mpn);
        }

        @Override
        public Supply findSupply(String supplierName, String supplierPartNumber)
        {
            return supplierNamed(supplierName).searchSupplierPartNumber(supplierPartNumber);
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
                uow.complete();
            }
        }

        @Override
        public void passivateService() throws Exception
        {

        }
    }
}
