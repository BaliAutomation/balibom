package ac.bali.bom.parts;

import org.apache.polygene.api.concern.Concerns;
import org.apache.polygene.api.entity.EntityBuilder;
import org.apache.polygene.api.identity.Identity;
import org.apache.polygene.api.identity.StringIdentity;
import org.apache.polygene.api.injection.scope.Structure;
import org.apache.polygene.api.mixin.Mixins;
import org.apache.polygene.api.query.QueryBuilder;
import org.apache.polygene.api.query.QueryBuilderFactory;
import org.apache.polygene.api.query.QueryExpressions;
import org.apache.polygene.api.unitofwork.NoSuchEntityException;
import org.apache.polygene.api.unitofwork.NoSuchEntityTypeException;
import org.apache.polygene.api.unitofwork.UnitOfWork;
import org.apache.polygene.api.unitofwork.UnitOfWorkFactory;
import org.apache.polygene.api.unitofwork.concern.UnitOfWorkConcern;
import org.apache.polygene.api.unitofwork.concern.UnitOfWorkPropagation;

@Mixins(PartsService.Mixin.class)
@Concerns(UnitOfWorkConcern.class)
public interface PartsService
{
    @UnitOfWorkPropagation
    Part findPartByIdentity(String id);

    @UnitOfWorkPropagation
    Part findOrCreatePart(Manufacturer manufacturer, String partNumber);

    @SuppressWarnings("resource")
    class Mixin
        implements PartsService
    {
        @Structure
        QueryBuilderFactory qbf;

        @Structure
        private UnitOfWorkFactory uowf;

        @Override
        public Part findPartByIdentity(String identity)
        {
            UnitOfWork uow = uowf.currentUnitOfWork();
            Identity id = StringIdentity.identityOf(identity);
            return uow.get(Part.class, id);
        }

        @Override
        public Part findOrCreatePart(Manufacturer manufacturer, String partNumber)
        {
            if( partNumber == null || partNumber.trim().length() < 4 )
                return null;
            UnitOfWork uow = uowf.currentUnitOfWork();
            Identity identity = StringIdentity.identityOf( manufacturer.shortName() + "_" + partNumber );
            try
            {
                return uow.get(Part.class, identity);
            } catch (NoSuchEntityException e)
            {
                EntityBuilder<Part> builder = uow.newEntityBuilder(Part.class, identity);
                Part part = builder.instance();
                part.manufacturer().set(manufacturer);
                part.mpn().set(partNumber);
                return builder.newInstance();
            }
        }
    }
}
