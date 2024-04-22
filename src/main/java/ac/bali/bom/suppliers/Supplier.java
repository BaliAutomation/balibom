package ac.bali.bom.suppliers;

import java.util.List;
import org.apache.polygene.api.common.UseDefaults;
import org.apache.polygene.api.identity.HasIdentity;
import org.apache.polygene.api.injection.scope.Service;
import org.apache.polygene.api.mixin.Mixins;
import org.apache.polygene.api.property.Property;
import org.apache.polygene.api.service.ServiceReference;
import org.qi4j.library.javafx.support.Order;
import org.qi4j.library.javafx.support.RenderAsName;

@Mixins(Supplier.Mixin.class)
public interface Supplier extends HasIdentity
{
    Supply searchSupplierPartNumber(String supplierPartNumber);

    Supply searchManufacturerPartNumber(String mf, String mpn);

    List<Supply> searchKeywords(String keywords);

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
        @Service
        Iterable<ServiceReference<SupplierProvider>> providers;

        @Override
        public Supply searchSupplierPartNumber(String supplierPartNumber)
        {
            return provider().searchSupplierPartNumber(supplierPartNumber);
        }

        @Override
        public Supply searchManufacturerPartNumber(String mf, String mpn)
        {
            return provider().searchManufacturerPartNumber(mf, mpn);
        }

        @Override
        public List<Supply> searchKeywords(String keywords)
        {
            return provider().searchKeywords(keywords);
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
