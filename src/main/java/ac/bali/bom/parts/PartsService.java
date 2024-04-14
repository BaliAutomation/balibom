package ac.bali.bom.parts;

import ac.bali.bom.connectivity.PartSupplyConnector;
import ac.bali.bom.products.BomItem;
import ac.bali.bom.suppliers.Supplier;
import ac.bali.bom.suppliers.SuppliersService;
import ac.bali.bom.ui.support.Action;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.polygene.api.concern.Concerns;
import org.apache.polygene.api.entity.EntityBuilder;
import org.apache.polygene.api.identity.Identity;
import org.apache.polygene.api.identity.StringIdentity;
import org.apache.polygene.api.injection.scope.Service;
import org.apache.polygene.api.injection.scope.Structure;
import org.apache.polygene.api.mixin.Mixins;
import org.apache.polygene.api.query.QueryBuilderFactory;
import org.apache.polygene.api.unitofwork.NoSuchEntityException;
import org.apache.polygene.api.unitofwork.UnitOfWork;
import org.apache.polygene.api.unitofwork.UnitOfWorkFactory;
import org.apache.polygene.api.unitofwork.concern.UnitOfWorkConcern;
import org.apache.polygene.api.unitofwork.concern.UnitOfWorkPropagation;
import org.apache.polygene.api.value.ValueBuilder;
import org.apache.polygene.api.value.ValueBuilderFactory;

@Mixins(PartsService.Mixin.class)
@Concerns(UnitOfWorkConcern.class)
public interface PartsService
{
    @UnitOfWorkPropagation
    Part findPart(String mf, String mpn);

    @SuppressWarnings("unused")
    @Action(label = "Update Supply")
    @UnitOfWorkPropagation
    void updateSupply(Part part);

    @UnitOfWorkPropagation
    void resolve(BomItem item);

    @SuppressWarnings("resource")
    class Mixin
        implements PartsService
    {
        @Structure
        ValueBuilderFactory vbf;

        @Structure
        QueryBuilderFactory qbf;

        @Structure
        private UnitOfWorkFactory uowf;

        @Service
        SuppliersService suppliersService;


        @Override
        public Part findPart(String manufacturer, String partNumber)
        {
            if (partNumber == null || partNumber.trim().length() < 4)
                return null;
            UnitOfWork uow = uowf.currentUnitOfWork();
            Identity identity = identityOf(manufacturer, partNumber);
            try
            {
                return uow.get(Part.class, identity);
            } catch (NoSuchEntityException e)
            {
                return null;
            }
        }

        @Override
        public void updateSupply(Part part)
        {
            Map<String, Supply> supply = suppliersService.suppliers().parallel()
                .map(s ->
                {
                    Supplier supplier = s.supplier();
                    Supply supl = part.supply().get().get(supplier.name().get());
                    String supplierPartNumber = supl.supplierPartNumber().get();
                    return new SimpleEntry<>(s.name(), createSupply(s, supplier, supplierPartNumber));
                })
                .filter(e -> e.getValue() != null )
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            part.supply().set(supply);
        }

        public Map<String, Supply> createSupply(BomItem item)
        {
            return suppliersService.suppliers().parallel()
                .map(s ->
                {
                    Supplier supplier = s.supplier();
                    String supplierPartNumber = getSupplierPartNumber(item, supplier);
                    if (supplierPartNumber == null)
                    {
                        return null;
                    }
                    return new SimpleEntry<>(s.name(), createSupply(s, supplier, supplierPartNumber));
                })
                .filter(Objects::nonNull)
                .filter(e -> e.getValue() != null )
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        }

        private Supply createSupply(PartSupplyConnector s, Supplier supplier, String supplierPartNumber)
        {
            Object fetched = s.searchSupplierPartNumber(supplierPartNumber);
            if( fetched != null )
            {
                ValueBuilder<Supply> supplyBuilder = vbf.newValueBuilder(Supply.class);
                Supply supplyPrototype = supplyBuilder.prototype();
                supplyPrototype.supplier().set(supplier);

                supplyPrototype.supplierPartNumber().set(supplierPartNumber);
                supplyPrototype.mf().set(s.manufacturerOf(fetched));
                supplyPrototype.mpn().set(s.mpnOf(fetched));
                supplyPrototype.availableSupply().set(s.availableSupplyOf(fetched));
                supplyPrototype.prices().set(s.createPriceList(fetched));
                supplyPrototype.reelSize().set(s.reelSizeOf(fetched));
                supplyPrototype.inStock().set(s.inStock(fetched));
                supplyPrototype.isReel().set(s.isReel(fetched));
                supplyPrototype.canShipWithInWeek().set(s.canShipWithInWeek(fetched));
                supplyPrototype.minBuyNumber().set(s.minBuyNumber(fetched));

                return supplyBuilder.newInstance();
            }
            return null;
        }

        private static String getSupplierPartNumber(BomItem bomItem, Supplier supplier)
        {
            for (String column : supplier.bomColumns().get())
            {
                String supplierPartNumber = bomItem.attributes().get().get(column);
                if (supplierPartNumber != null)
                    return supplierPartNumber;
            }
            return null;
        }

        @Override
        public void resolve(BomItem item)
        {
            String mf = item.mf().get();
            String mpn = item.mpn().get();
            Part existingpart = findPart(mf, mpn);
            if (existingpart != null)
            {
                return;
            }
            List<String> errors = new ArrayList<>();
            Map<String, Supply> supply = createSupply(item);
            for (Supply s : supply.values())
            {
                String smf = s.mf().get();
                String smpn = s.mpn().get();
                if (mf == null || mf.length() == 0)
                {
                    mf = smf;
                } else
                {
                    if (!smf.equalsIgnoreCase(mf))
                    {
                        errors.add("Manufacturer from " + s.supplier().get().name() + " is " + smf + " and doesn't match BOM MF of " + mf);
                    }
                }
                if( mpn == null || mpn.length() == 0 )
                {
                    mpn = smpn;
                } else {
                    if (!smpn.equalsIgnoreCase(mpn))
                    {
                        errors.add("MPN from " + s.supplier().get().name() + " is " + s.mpn().get() + " and doesn't match BOM MPN of " + mpn);
                    }
                }
                existingpart = findPart(mf, mpn);
                if( existingpart != null )
                {
                    existingpart.supply().set(supply);
                    return;
                }
            }
            buildNewPart(mf, mpn, supply, errors);
        }

        private void buildNewPart(String mf, String mpn, Map<String, Supply> supply, List<String> errors)
        {
            UnitOfWork uow = uowf.currentUnitOfWork();
            EntityBuilder<Part> builder = uow.newEntityBuilder(Part.class, identityOf(mf, mpn));
            Part partPrototype = builder.instance();
            partPrototype.manufacturer().set(mf);
            partPrototype.mpn().set(mpn);
            partPrototype.supply().set(supply);
            partPrototype.errors().set(errors);
            builder.newInstance();
        }

        private Identity identityOf(String mf, String mpn)
        {
            return StringIdentity.identityOf("part_" + mf + "." + mpn);
        }
    }
}
