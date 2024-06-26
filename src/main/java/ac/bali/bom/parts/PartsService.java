package ac.bali.bom.parts;

import ac.bali.bom.manufacturers.Manufacturer;
import ac.bali.bom.manufacturers.ManufacturersService;
import ac.bali.bom.supply.Supplier;
import ac.bali.bom.supply.SuppliersService;
import ac.bali.bom.supply.Supply;
import ac.bali.bom.supply.manual.ManualSupplier;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
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
import org.qi4j.library.crudui.Action;

import static org.apache.polygene.api.unitofwork.concern.UnitOfWorkPropagation.Propagation.MANDATORY;
import static org.qi4j.library.crudui.ActionScope.composite;

@Mixins(PartsService.Mixin.class)
@Concerns(UnitOfWorkConcern.class)
public interface PartsService
{
    @SuppressWarnings("unused")
    @UnitOfWorkPropagation(MANDATORY)
    Part findPart(Manufacturer mf, String mpn);

    @SuppressWarnings("unused")
    @Action(label = "Update Supply", scope = composite)
    @UnitOfWorkPropagation(MANDATORY)
    void updateSupply(Part part);

    @UnitOfWorkPropagation(MANDATORY)
    Part resolve(String manufacturer, String mpn, Map<String, String> attributes, List<String> errorReport);

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

        @Service
        ManufacturersService manufacturers;


        @Override
        public Part findPart(Manufacturer manufacturer, String partNumber)
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
            Map<String, Supply> newSupply = new HashMap<>(part.supply().get());
            for (Map.Entry<String, Supply> entry : part.supply().get().entrySet())
            {
                Supply supply = suppliersService.findSupply(entry.getKey(), entry.getValue().supplierPartNumber().get(), part.parameters().get());
                if (supply != null)
                {
                    newSupply.put(entry.getKey(), supply);
                }
            }
            for (Supplier s : suppliersService.suppliers())
            {
                String supplierName = s.name().get();
                Supply supply = newSupply.get(supplierName);
                if (supply == null)
                {
                    supply = suppliersService.findSupply(supplierName, part.manufacturer().get(), part.mpn().get(), part.parameters().get());
                    if (supply != null)
                    {
                        newSupply.put(supplierName, supply);
                    }
                }
            }
            part.supply().set(newSupply);
        }

        public Map<String, Supply> createSupply(String manufacturer, String mpn, Map<String, String> attributes)
        {
            Manufacturer mf = manufacturers.findManufacturer(manufacturer);
            Map<String, Supply> result = new HashMap<>();
            // Get supply from Supplier Part Number
            for (Supplier supplier : suppliersService.suppliers())
            {
                if (supplier.enabled().get())
                {
                    String supplierName = supplier.name().get();
                    String supplierPartNumber = getSupplierPartNumber(attributes, supplier);
                    Supply supply;
                    if (supplierPartNumber != null)
                    {
                        supply = suppliersService.findSupply(supplierName, supplierPartNumber, attributes);
                    } else
                    {
                        supply = suppliersService.findSupply(supplierName, mf, mpn, attributes);
                    }
                    if (supply != null)
                        result.put(supplierName, supply);
                }
            }
            // Get supply from keyword search, desperate attempt
            if (result.size() == 0)
            {
                List<Supply> supplies = suppliersService.searchSupply(mf, mpn);
                for (Supply s : supplies)
                    result.put(s.supplier().get().name().get(), s);
            }
            // Last attempt, Manual entry
            if (result.size() == 0)
            {
                Supplier supplier = suppliersService.suppliers()
                    .stream()
                    .filter(s -> s.name().get().equals(ManualSupplier.NAME))
                    .findAny().orElse(null);
                if (supplier == null)
                    return result;

                // Direct access to by-pass whether enabled()
                supplier.provider().searchSupplierPartNumber(supplier, "-----------", attributes);  // clear
                supplier.bomColumns().get().stream().forEach(col ->
                {
                    String sourceCandidate = attributes.get(col);
                    if (sourceCandidate != null)
                    {
                        supplier.provider().searchSupplierPartNumber(supplier, sourceCandidate, attributes);
                    }
                });
                Supply supply = supplier.provider().searchManufacturerPartNumber(supplier, mf.identifier().get(), mpn, attributes);
                if (supply == null)
                    result.put(ManualSupplier.NAME, supply);
            }
            return result;
        }


        private static String getSupplierPartNumber(Map<String, String> attributes, Supplier supplier)
        {
            for (String column : supplier.bomColumns().get())
            {
                String supplierPartNumber = attributes.get(column);
                if (supplierPartNumber != null && supplierPartNumber.trim().length() > 0)
                    return supplierPartNumber.trim();
            }
            return null;
        }

        @Override
        public Part resolve(String manufacturer, String mpn, Map<String, String> attributes, List<String> errorReport)
        {
            Manufacturer mf = manufacturers.findManufacturer(manufacturer);
            Part existingpart = findPart(mf, mpn);
            if (existingpart != null)
            {
                return existingpart;
            }
            Map<String, Supply> supply = createSupply(manufacturer, mpn, attributes);
            for (Supply s : supply.values())
            {
                if (s == null)
                    continue;
                Manufacturer smf = s.mf().get();
                String smpn = s.mpn().get();
                if (mf == null)
                {
                    mf = smf;
                } else
                {
                    if (!smf.equals(mf))
                    {
                        errorReport.add("Manufacturer from " + s.supplier().get().name().get() + " is " + smf + " and doesn't match BOM MF of " + mf);
                    }
                }
                if (mpn == null || mpn.length() == 0)
                {
                    mpn = smpn;
                } else
                {
                    if (!smpn.equalsIgnoreCase(mpn))
                    {
                        errorReport.add("MPN from " + s.supplier().get().name().get() + " is " + s.mpn().get() + " and doesn't match BOM MPN of " + mpn);
                    }
                }
                existingpart = findPart(mf, mpn);
                if (existingpart != null)
                {
                    existingpart.supply().set(supply);
                    return existingpart;
                }
            }
            if (supply.size() > 0)     // Don't add part if we have no supplier,
                existingpart = buildNewPart(mf, mpn, supply);
            return existingpart;
        }

        private Part buildNewPart(Manufacturer mf, String mpn, Map<String, Supply> supply)
        {
            UnitOfWork uow = uowf.currentUnitOfWork();
            mpn = mpn.trim();
            if (mpn.length() == 0)
            {
                return null;
            }
            EntityBuilder<Part> builder = uow.newEntityBuilder(Part.class, identityOf(mf, mpn));
            Part partPrototype = builder.instance();
            if (supply.size() > 0)
            {
                Collection<Supply> supplies = supply.values();
                Optional<String> intro = supplies.stream()
                    .filter(Objects::nonNull)
                    .map(s -> s.productIntro().get())
                    .max(Comparator.comparingInt(String::length));      // Find the longest part introduction, and use that.
                partPrototype.partIntro().set(intro.orElse(""));
            }
            partPrototype.parameters().set(mergeParameters(supply));
            partPrototype.manufacturer().set(mf);
            partPrototype.mpn().set(mpn);
            partPrototype.supply().set(supply);
            return builder.newInstance();
        }

        private Map<String, String> mergeParameters(Map<String, Supply> supplies)
        {
            // TODO Check that the same parameters from different suppliers have the same values. This can be tricky,
            //  as texts may vary a little bit, but mean the same thing. Report errors to error() property.
            Map<String, String> result = new HashMap<>();
            supplies.values()                                        // get the Supply instances
                .stream()
                .filter(Objects::nonNull)
                .forEach(supplier ->
                {
                    supplier.parameters().get().entrySet().forEach(param ->
                    {
                        String key = param.getKey().trim();
                        String value = param.getValue().trim();
                        if (key.equals("-"))
                        {
                            System.err.println("Error in parameters: " + key + "=" + value);
                        } else if (value.equals("-") || value.length() == 0)
                        {
                            // skip the parameters that are unspecified
                        } else
                        {
                            result.put(key, value);
                        }
                    });
                });
            return result;
        }

        private Identity identityOf(Manufacturer mf, String mpn)
        {
            return StringIdentity.identityOf("part/" + mf.identifier().get() + "." + mpn);
        }
    }
}
