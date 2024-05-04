package ac.bali.bom.parts;

import ac.bali.bom.products.BomItem;
import ac.bali.bom.suppliers.Supplier;
import ac.bali.bom.suppliers.SuppliersService;
import ac.bali.bom.suppliers.Supply;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
import org.apache.polygene.api.value.ValueBuilderFactory;
import org.qi4j.library.javafx.support.Action;

import static org.apache.polygene.api.unitofwork.concern.UnitOfWorkPropagation.Propagation.MANDATORY;
import static org.qi4j.library.javafx.support.ActionScope.composite;

@Mixins(PartsService.Mixin.class)
@Concerns(UnitOfWorkConcern.class)
public interface PartsService
{
    @UnitOfWorkPropagation(MANDATORY)
    Part findPart(String mf, String mpn);

    @SuppressWarnings("unused")
    @Action(label = "Update Supply", scope = composite)
    @UnitOfWorkPropagation(MANDATORY)
    void updateSupply(Part part);

    @Action(label = "Delete", scope = composite)
    @UnitOfWorkPropagation(MANDATORY)
    void delete(Part part);

    @UnitOfWorkPropagation(MANDATORY)
    void resolve(BomItem item);

    Map<String, String> findAttributes(Part part);

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
            Map<String, Supply> newSupply = new HashMap<>();
            for (Map.Entry<String, Supply> entry : part.supply().get().entrySet())
            {
                Supply supply = suppliersService.findSupply(entry.getKey(), entry.getValue().supplierPartNumber().get());
                if (supply != null)
                    newSupply.put(entry.getKey(), supply);
            }
            UnitOfWork uow = uowf.currentUnitOfWork();
            Part entity = uow.toEntity(Part.class, part);
            entity.supply().set(newSupply);
        }

        @Override
        public void delete(Part part)
        {
            UnitOfWork uow = uowf.currentUnitOfWork();
            uow.remove(part);
        }

        public Map<String, Supply> createSupply(BomItem item)
        {
            String mf = item.mf().get();
            String mpn = item.mpn().get();
            Map<String, Supply> result = new HashMap<>();
            // Get supply from Supplier Part Number
            for (Supplier supplier : suppliersService.suppliers())
            {
                String supplierName = supplier.name().get();
                String supplierPartNumber = getSupplierPartNumber(item, supplier);
                Supply supply;
                if (supplierPartNumber != null)
                {
                    supply = suppliersService.findSupply(supplierName, supplierPartNumber);
                } else
                {
                    supply = suppliersService.findSupply(supplierName, mf, mpn);
                }
                if (supply != null)
                    result.put(supplierName, supply);
            }
            // Get supply from MF/MPN
            if (result.size() == 0)
            {
                List<Supply> supplies = suppliersService.searchSupply(mf, mpn);
                for (Supply s : supplies)
                {
                    if (s != null)
                        result.put(s.supplier().get().name().get(), s);
                }
            }
            return result;
        }


        private static String getSupplierPartNumber(BomItem bomItem, Supplier supplier)
        {
            for (String column : supplier.bomColumns().get())
            {
                String supplierPartNumber = bomItem.attributes().get().get(column);
                if (supplierPartNumber != null && supplierPartNumber.trim().length() > 0)
                    return supplierPartNumber.trim();
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
                if (s == null)
                    continue;
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
                if (mpn == null || mpn.length() == 0)
                {
                    mpn = smpn;
                } else
                {
                    if (!smpn.equalsIgnoreCase(mpn))
                    {
                        errors.add("MPN from " + s.supplier().get().name() + " is " + s.mpn().get() + " and doesn't match BOM MPN of " + mpn);
                    }
                }
                existingpart = findPart(mf, mpn);
                if (existingpart != null)
                {
                    existingpart.supply().set(supply);
                    return;
                }
            }
            if (supply.size() > 0)     // Don't add part if we have no supplier,
                buildNewPart(mf, mpn, supply, errors);
        }

        private void buildNewPart(String mf, String mpn, Map<String, Supply> supply, List<String> errors)
        {
            UnitOfWork uow = uowf.currentUnitOfWork();
            mf = mf.trim();
            mpn = mpn.trim();
            if (mf.length() == 0)
            {
                return;
            }
            if (mpn.length() == 0)
            {
                return;
            }
            EntityBuilder<Part> builder = uow.newEntityBuilder(Part.class, identityOf(mf, mpn));
            Part partPrototype = builder.instance();
            if (supply.size() > 0)
            {
                Collection<Supply> supplies = supply.values();
                Optional<String> intro = supplies.stream()
                    .map(s -> s.productIntro().get())
                    .max(Comparator.comparingInt(String::length));      // Find the longest part introduction, and use that.
                partPrototype.partIntro().set(intro.orElse(""));
            }
            partPrototype.parameters().set(mergeParameters(supply));
            partPrototype.manufacturer().set(mf);
            partPrototype.mpn().set(mpn);
            partPrototype.supply().set(supply);
            partPrototype.errors().set(errors);
            builder.newInstance();
        }

        private Map<String, String> mergeParameters(Map<String, Supply> supplies)
        {
            // TODO Check that the same parameters from different suppliers have the same values. Report errors to error() property.
            return supplies.values()                                        // get the Supply instances
                .stream()                                                   // stream the Supply instances
                .flatMap(s -> s.parameters().get().entrySet().stream())     // stream the parameter pairs
                .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getValue));   // collect into a Map.
        }

        @Override
        public Map<String, String> findAttributes(Part part)
        {
            return part.parameters().get();
        }

        private Identity identityOf(String mf, String mpn)
        {
            return StringIdentity.identityOf("part_" + mf + "." + mpn);
        }
    }
}
