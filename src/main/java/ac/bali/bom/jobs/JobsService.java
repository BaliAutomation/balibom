package ac.bali.bom.jobs;

import ac.bali.bom.products.Product;
import org.apache.polygene.api.entity.EntityBuilder;
import org.apache.polygene.api.injection.scope.Structure;
import org.apache.polygene.api.mixin.Mixins;
import org.apache.polygene.api.unitofwork.UnitOfWork;
import org.apache.polygene.api.unitofwork.UnitOfWorkFactory;
import org.apache.polygene.api.unitofwork.concern.UnitOfWorkPropagation;
import org.qi4j.library.javafx.support.Action;

@SuppressWarnings("unused")
@Mixins(JobsService.Mixin.class)
public interface JobsService
{
    @Action(label="Create Job...")
    @UnitOfWorkPropagation(usecase = "Create Job")
    Job createJob(Product product, int quantity);

    class Mixin
        implements JobsService
    {
        @Structure
        UnitOfWorkFactory uowf;

        @Override
        public Job createJob(Product product, int quantity)
        {
            UnitOfWork uow = uowf.currentUnitOfWork();
            EntityBuilder<Job> builder = uow.newEntityBuilder(Job.class);
            Job proto = builder.instance();
            proto.product().set(product);
            proto.quantity().set(quantity);
            return builder.newInstance();
        }
    }
}
