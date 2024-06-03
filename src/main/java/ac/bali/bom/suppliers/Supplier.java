package ac.bali.bom.suppliers;

import ac.bali.bom.manufacturers.Manufacturer;
import ac.bali.bom.suppliers.oauth2.OAuth2LoginConcern;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.polygene.api.association.Association;
import org.apache.polygene.api.common.Optional;
import org.apache.polygene.api.common.UseDefaults;
import org.apache.polygene.api.concern.Concerns;
import org.apache.polygene.api.identity.HasIdentity;
import org.apache.polygene.api.injection.scope.Service;
import org.apache.polygene.api.injection.scope.This;
import org.apache.polygene.api.mixin.Mixins;
import org.apache.polygene.api.property.Property;
import org.apache.polygene.api.service.ServiceReference;
import org.qi4j.library.crudui.Height;
import org.qi4j.library.crudui.Order;
import org.qi4j.library.crudui.RenderAsName;

@Mixins(Supplier.Mixin.class)
@Concerns({OAuth2LoginConcern.class})
public interface Supplier extends HasIdentity
{
    SupplierProvider provider();

    @LoginRequired
    Supply searchSupplierPartNumber(String supplierPartNumber, Map<String,String> attributes);

    @LoginRequired
    Supply searchManufacturerPartNumber(Manufacturer mf, String mpn, Map<String,String> attributes);

    @LoginRequired
    List<Supply> searchKeywords(String keywords);

    Property<Boolean> enabled();

    @RenderAsName
    @Order(1)
    Property<String> name();

    @UseDefaults
    @Height(min=100, pref=100, max=300)
    Property<Set<String>> bomColumns();

    @Order(2)
    Property<Map<String,String>> hosts();

    Property<Map<String,String>> paths();

    Property<String> website();

    @Optional
    Association<AuthenticationMethod> authentication();

    abstract class Mixin
        implements Supplier
    {

        @This
        Supplier meAsSupplier;

        @Service
        Iterable<ServiceReference<SupplierProvider>> providers;

        @Override
        public Supply searchSupplierPartNumber(String supplierPartNumber, Map<String,String> attributes)
        {
            if (!enabled().get())
            {
                return null;
            }
            return provider().searchSupplierPartNumber(meAsSupplier, supplierPartNumber, attributes);
        }

        @Override
        public Supply searchManufacturerPartNumber(Manufacturer mf, String mpn, Map<String,String> attributes)
        {
            if (!enabled().get())
            {
                return null;
            }
            return provider().searchManufacturerPartNumber(meAsSupplier, mf.identifier().get(), mpn, attributes);
        }

        @Override
        public List<Supply> searchKeywords(String keywords)
        {
            if (!enabled().get())
            {
                return null;
            }
            return provider().searchKeywords(meAsSupplier, keywords);
        }

        public SupplierProvider provider()
        {
            for (ServiceReference<SupplierProvider> ref : providers)
            {
                if (ref.identity().toString().equalsIgnoreCase(name().get()))
                {
                    if (ref.isAvailable() && ref.isActive())
                    {
                        return ref.get();
                    }
                }
            }
            throw new IllegalArgumentException(name().get() + " doesn't have a SupplierProvider available.");
        }
    }
}
