package ac.bali.bom.manufacturers;

import org.apache.polygene.api.concern.Concerns;
import org.apache.polygene.api.identity.Identity;
import org.apache.polygene.api.identity.StringIdentity;
import org.apache.polygene.api.injection.scope.Structure;
import org.apache.polygene.api.mixin.Mixins;
import org.apache.polygene.api.unitofwork.NoSuchEntityException;
import org.apache.polygene.api.unitofwork.UnitOfWork;
import org.apache.polygene.api.unitofwork.UnitOfWorkFactory;
import org.apache.polygene.api.unitofwork.concern.UnitOfWorkConcern;
import org.apache.polygene.api.unitofwork.concern.UnitOfWorkPropagation;
import org.apache.polygene.api.value.ValueBuilder;
import org.apache.polygene.api.value.ValueBuilderFactory;

import static org.apache.polygene.api.unitofwork.concern.UnitOfWorkPropagation.Propagation.MANDATORY;

@SuppressWarnings("resource")
@Mixins(ManufacturersService.Mixin.class)
@Concerns( UnitOfWorkConcern.class )
public interface ManufacturersService
{
    @UnitOfWorkPropagation(MANDATORY)
    Manufacturer findManufacturer(String identifier);

    @UnitOfWorkPropagation(MANDATORY)
    void addManufacturer(Manufacturer manufacturer);

    @UnitOfWorkPropagation(MANDATORY)
    void removeManufacturer(Manufacturer manufacturer);

    @UnitOfWorkPropagation(MANDATORY)
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
                entity.identifier().set(identifier);
                entity.fullName().set(identifier);
                return entity;
            }
        }

        @Override
        public void addManufacturer(Manufacturer manufacturer)
        {
            UnitOfWork uow = uowf.currentUnitOfWork();
            Identity identity = StringIdentity.identityOf(manufacturer.identifier().get());
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
            builder.prototype().identifier().set("<new>");
            return builder.newInstance();
        }
    }
}
