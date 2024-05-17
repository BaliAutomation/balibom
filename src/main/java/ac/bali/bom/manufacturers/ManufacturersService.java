package ac.bali.bom.manufacturers;

import java.util.List;
import org.apache.polygene.api.concern.Concerns;
import org.apache.polygene.api.identity.Identity;
import org.apache.polygene.api.identity.StringIdentity;
import org.apache.polygene.api.injection.scope.Structure;
import org.apache.polygene.api.mixin.Mixins;
import org.apache.polygene.api.query.Query;
import org.apache.polygene.api.query.QueryBuilder;
import org.apache.polygene.api.query.QueryBuilderFactory;
import org.apache.polygene.api.unitofwork.NoSuchEntityException;
import org.apache.polygene.api.unitofwork.UnitOfWork;
import org.apache.polygene.api.unitofwork.UnitOfWorkFactory;
import org.apache.polygene.api.unitofwork.concern.UnitOfWorkConcern;
import org.apache.polygene.api.unitofwork.concern.UnitOfWorkPropagation;
import org.apache.polygene.api.value.ValueBuilder;
import org.apache.polygene.api.value.ValueBuilderFactory;

import static org.apache.polygene.api.query.QueryExpressions.contains;
import static org.apache.polygene.api.query.QueryExpressions.templateFor;
import static org.apache.polygene.api.unitofwork.concern.UnitOfWorkPropagation.Propagation.MANDATORY;

@SuppressWarnings("resource")
@Mixins(ManufacturersService.Mixin.class)
@Concerns(UnitOfWorkConcern.class)
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

        @Structure
        private QueryBuilderFactory qbf;

        @Override
        public Manufacturer findManufacturer(String identifier)
        {
            Manufacturer mf = loadManufacturer(identifier);
            if (mf == null)
            {
                mf = queryForManufacturer(identifier);
                if (mf == null)
                {
                    mf = createNewManufacturer(identifier);
                }
            }
            return mf;
        }

        private Manufacturer loadManufacturer(String identifier)
        {
            UnitOfWork uow = uowf.currentUnitOfWork();
            Identity identity = StringIdentity.identityOf(identifier);
            try
            {
                return uow.get(Manufacturer.class, identity);
            } catch (NoSuchEntityException e)
            {
                return null;
            }
        }

        private Manufacturer queryForManufacturer(String identifier)
        {
            UnitOfWork uow = uowf.currentUnitOfWork();
            QueryBuilder<Manufacturer> qb = qbf.newQueryBuilder(Manufacturer.class);
            Manufacturer template = templateFor(Manufacturer.class);
            qb = qb.where(contains(template.alternateNames(), identifier));
            Query<Manufacturer> query = uow.newQuery(qb);
            List<Manufacturer> list = query.stream().toList();
            if (list.size() > 1)
                System.err.println("WARNING: Manufacturer: More than one Manufacturer was found with the alias: " + identifier + ", in " + list);
            if (list.size() == 0)
                return null;
            return list.get(0);
        }

        private Manufacturer createNewManufacturer(String identifier)
        {
            UnitOfWork uow = uowf.currentUnitOfWork();
            Identity identity = StringIdentity.identityOf(identifier);
            Manufacturer entity = uow.newEntity(Manufacturer.class, identity);
            entity.identifier().set(identifier);
            entity.fullName().set(identifier);
            return entity;
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
