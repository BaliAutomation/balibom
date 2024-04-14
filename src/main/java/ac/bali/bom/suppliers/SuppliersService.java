package ac.bali.bom.suppliers;

import ac.bali.bom.connectivity.PartSupplyConnector;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.apache.polygene.api.injection.scope.Service;
import org.apache.polygene.api.mixin.Mixins;
import org.apache.polygene.api.service.ServiceReference;

@Mixins(SuppliersService.Mixin.class)
public interface SuppliersService
{
    Stream<PartSupplyConnector> suppliers();

    class Mixin
        implements SuppliersService
    {
        @Service
        Iterable<ServiceReference<PartSupplyConnector>> suppliers;

        public Stream<PartSupplyConnector> suppliers()
        {
            return StreamSupport.stream(suppliers.spliterator(), true)
                .filter(ServiceReference::isAvailable)
                .filter(ServiceReference::isActive)
                .map(ServiceReference::get)
                ;
        }

    }
}
