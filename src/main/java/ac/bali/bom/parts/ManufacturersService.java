package ac.bali.bom.parts;

import org.apache.polygene.api.activation.Activators;
import org.apache.polygene.api.composite.TransientBuilder;
import org.apache.polygene.api.composite.TransientBuilderFactory;
import org.apache.polygene.api.concern.Concerns;
import org.apache.polygene.api.entity.EntityBuilder;
import org.apache.polygene.api.identity.Identity;
import org.apache.polygene.api.identity.StringIdentity;
import org.apache.polygene.api.injection.scope.Structure;
import org.apache.polygene.api.mixin.Mixins;
import org.apache.polygene.api.service.ServiceActivation;
import org.apache.polygene.api.unitofwork.NoSuchEntityException;
import org.apache.polygene.api.unitofwork.NoSuchEntityTypeException;
import org.apache.polygene.api.unitofwork.UnitOfWork;
import org.apache.polygene.api.unitofwork.UnitOfWorkFactory;
import org.apache.polygene.api.unitofwork.concern.UnitOfWorkConcern;
import org.apache.polygene.api.unitofwork.concern.UnitOfWorkPropagation;
import org.apache.polygene.api.value.ValueBuilder;
import org.apache.polygene.api.value.ValueBuilderFactory;

import java.util.List;

@SuppressWarnings("resource")
@Mixins(ManufacturersService.Mixin.class)
@Concerns( UnitOfWorkConcern.class )
public interface ManufacturersService
{
    @UnitOfWorkPropagation
    Manufacturer findManufacturer(String identifier);

    @UnitOfWorkPropagation
    void addManufacturer(Manufacturer manufacturer);

    @UnitOfWorkPropagation
    void removeManufacturer(Manufacturer manufacturer);

    @UnitOfWorkPropagation
    Manufacturer newManufacturer();

    class Mixin
        implements ManufacturersService
    {
        @Structure
        private ValueBuilderFactory vbf;

        @Structure
        private UnitOfWorkFactory uowf;

        @Override
        public Manufacturer findManufacturer(String identifier)
        {
            UnitOfWork uow = uowf.currentUnitOfWork();
            Identity identity = StringIdentity.identityOf(identifier);
            try
            {
                return uow.get(Manufacturer.class, identity);
            } catch (NoSuchEntityException e)
            {
                Manufacturer entity = uow.newEntity(Manufacturer.class, identity);
                entity.shortName().set(identifier);
                entity.fullName().set(identifier);
                return entity;
            }
        }

        @Override
        public void addManufacturer(Manufacturer manufacturer)
        {
            UnitOfWork uow = uowf.currentUnitOfWork();
            Identity identity = StringIdentity.identityOf(manufacturer.shortName().get());
            uow.newEntity(Manufacturer.class, identity);
        }

        @Override
        public void removeManufacturer(Manufacturer manufacturer)
        {
            UnitOfWork uow = uowf.currentUnitOfWork();

            uow.remove(manufacturer);
        }

        @Override
        public Manufacturer newManufacturer()
        {
            ValueBuilder<Manufacturer> builder = vbf.newValueBuilder(Manufacturer.class);
            builder.prototype().shortName().set("<new>");
            return builder.newInstance();
        }
    }
}
