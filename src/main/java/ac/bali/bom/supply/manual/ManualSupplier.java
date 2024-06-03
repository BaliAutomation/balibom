package ac.bali.bom.supply.manual;

import ac.bali.bom.manufacturers.Manufacturer;
import ac.bali.bom.manufacturers.ManufacturersService;
import ac.bali.bom.parts.Price;
import ac.bali.bom.supply.Supplier;
import ac.bali.bom.supply.SupplierProvider;
import ac.bali.bom.supply.Supply;
import ac.bali.bom.supply.manual.model.Product;
import ac.bali.bom.view.ManualProductSearch;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import org.apache.polygene.api.entity.EntityBuilder;
import org.apache.polygene.api.identity.Identity;
import org.apache.polygene.api.identity.StringIdentity;
import org.apache.polygene.api.injection.scope.Service;
import org.apache.polygene.api.injection.scope.Structure;
import org.apache.polygene.api.mixin.Mixins;
import org.apache.polygene.api.unitofwork.NoSuchEntityException;
import org.apache.polygene.api.unitofwork.UnitOfWork;
import org.apache.polygene.api.unitofwork.UnitOfWorkFactory;
import org.apache.polygene.api.value.ValueBuilder;
import org.apache.polygene.api.value.ValueBuilderFactory;

@Mixins(ManualSupplier.Mixin.class)
public interface ManualSupplier extends SupplierProvider
{
    String NAME = "Manually";

    class Mixin
        implements ManualSupplier
    {
        public static final Identity IDENTITY = StringIdentity.identityOf("supplier/ManualEntry");

        @Structure
        UnitOfWorkFactory uowf;

        @Structure
        ValueBuilderFactory vbf;

        @Service
        ManualProductSearch products;

        @Service
        ManufacturersService manufacturers;

        private final List<String> sources = new ArrayList<>();

        @Override
        public Supply searchSupplierPartNumber(Supplier supplier, String supplierPartNumber, Map<String,String> attributes)
        {
            if( "-----------".equals(supplierPartNumber) )  // special meaning
            {
                sources.clear();
                return null;
            }
            if( supplierPartNumber.trim().length() == 0 )
                return null;
            sources.add(supplierPartNumber);
            return null;
        }

        @Override
        public Supply searchManufacturerPartNumber(Supplier supplier, String mf, String mpn, Map<String,String> attributes)
        {
            if (mf == null || mf.trim().length() == 0)
            {
                return null;
            }
            if (mpn == null || mpn.trim().length() == 0)
            {
                return null;
            }

            Product part = products.searchPart(supplier, mf, mpn, sources, attributes);
            if( part == null )
                return null;
            return createSupply(supplier, part);
        }

        @Override
        public List<Supply> searchKeywords(Supplier supplier, String keywords)
        {
            return null;
        }

        private Supply createSupply(Supplier supplier, Product product)
        {
            ValueBuilder<Supply> builder = vbf.newValueBuilder(Supply.class);
            Supply p = builder.prototype();
            p.updatedOn().set(LocalDate.now());
            Manufacturer mf = manufacturers.findManufacturer(product.manufacturer().get());
            p.mf().set(mf);
            p.mpn().set(product.manufacturerPartNumber().get());
            p.supplier().set(supplier);
            p.supplierPartNumber().set(product.supplierPartNumber().get());
            p.productIntro().set(product.partDescription().get());
            p.availableSupply().set(product.quantityAvailable().get());
            p.inStock().set(product.quantityAvailable().get());
            p.canShipWithInWeek().set(product.quantityAvailable().get());
            p.reelSize().set(product.reelSize().get());
            p.isReel().set(product.isReel().get());
            p.images().set(Collections.emptyList());
            p.datasheet().set(product.datasheetUrl().get());
            p.parameters().set(getParameters(product));
            p.prices().set(getPriceList(product));
            p.minBuyNumber().set(product.minimumPurchase().get());
            return builder.newInstance();
        }

        private static Map<String, String> getParameters(Product product)
        {
            Map<String, String> result = new HashMap<>();
            return result;
        }

        private SortedSet<Price> getPriceList(Product product)
        {
            TreeSet<Price> result = new TreeSet<>(new Price.PriceComparator());
            ValueBuilder<Price> builder = vbf.newValueBuilder(Price.class);
            Price prototype = builder.prototype();
            for( Map.Entry<Integer, BigDecimal> entry : product.prices().get().entrySet() )
            {
                prototype.quantity().set(entry.getKey());
                prototype.price().set(entry.getValue());
                result.add(builder.newInstance());
            }
            return result;
        }

        public static void createSupplier(UnitOfWork uow) throws Exception
        {
            try
            {
                Supplier supplier = uow.get(Supplier.class, IDENTITY);
            } catch (NoSuchEntityException e)
            {
                EntityBuilder<Supplier> eb = uow.newEntityBuilder(Supplier.class, IDENTITY);
                Supplier instance = eb.instance();
                instance.name().set(NAME);
                instance.enabled().set(false);
                Map<String, String> hosts = new HashMap<>();
                instance.hosts().set(hosts);

                Map<String, String> paths = new HashMap<>();
                instance.paths().set(paths);

                instance.website().set("");

                instance.bomColumns().get().add(NAME);
                instance.bomColumns().get().add("Manual");
                instance.bomColumns().get().add("OrderFrom");
                eb.newInstance();
            }
        }
    }
}