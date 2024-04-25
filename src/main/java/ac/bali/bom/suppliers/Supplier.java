package ac.bali.bom.suppliers;

import ac.bali.bom.suppliers.oauth2.OAuth2Authentication;
import java.util.List;
import java.util.Map;
import org.apache.polygene.api.common.Optional;
import org.apache.polygene.api.common.UseDefaults;
import org.apache.polygene.api.concern.Concerns;
import org.apache.polygene.api.identity.HasIdentity;
import org.apache.polygene.api.injection.scope.Service;
import org.apache.polygene.api.injection.scope.This;
import org.apache.polygene.api.mixin.Mixins;
import org.apache.polygene.api.property.Property;
import org.apache.polygene.api.service.ServiceReference;
import org.qi4j.library.javafx.support.Order;
import org.qi4j.library.javafx.support.RenderAsName;

@Mixins(Supplier.Mixin.class)
@Concerns(OAuth2LoginConcern.class)
public interface Supplier extends HasIdentity, OAuth2Authentication
{
    @LoginRequired
    Supply searchSupplierPartNumber(String supplierPartNumber);

    @LoginRequired
    Supply searchManufacturerPartNumber(String mf, String mpn);

    @LoginRequired
    List<Supply> searchKeywords(String keywords);

    Property<Boolean> enabled();

    @RenderAsName
    @Order(1)
    Property<String> name();

    @Order(2)
    Property<String> website();

    @Order(3)
    Property<String> searchApi();

    @Order(4)
    Property<String> productDetailsApi();

    @Order(5)
    Property<String> orderingApi();

    @UseDefaults
    Property<List<String>> bomColumns();

    abstract class Mixin
        implements Supplier
    {
        @This
        Supplier meAsSupplier;

        @Service
        Iterable<ServiceReference<SupplierProvider>> providers;

        @Override
        public Supply searchSupplierPartNumber(String supplierPartNumber)
        {
            if (!enabled().get())
            {
                return null;
            }
            return provider().searchSupplierPartNumber(meAsSupplier, supplierPartNumber);
        }

        @Override
        public Supply searchManufacturerPartNumber(String mf, String mpn)
        {
            if (!enabled().get())
            {
                return null;
            }
            return provider().searchManufacturerPartNumber(meAsSupplier, mf, mpn);
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

        private SupplierProvider provider()
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
