package ac.bali.bom.jobs;

import ac.bali.bom.customers.Customer;
import ac.bali.bom.products.Product;
import java.time.LocalDate;
import java.util.List;
import org.apache.polygene.api.entity.EntityBuilder;
import org.apache.polygene.api.injection.scope.Structure;
import org.apache.polygene.api.mixin.Mixins;
import org.apache.polygene.api.unitofwork.UnitOfWork;
import org.apache.polygene.api.unitofwork.UnitOfWorkFactory;
import org.apache.polygene.api.unitofwork.concern.UnitOfWorkPropagation;
import org.qi4j.library.javafx.support.Action;

import static org.apache.polygene.api.unitofwork.concern.UnitOfWorkPropagation.Propagation.MANDATORY;

@SuppressWarnings("unused")
@Mixins(JobsService.Mixin.class)
public interface JobsService
{
    @Action(label="Create Job...")
    @UnitOfWorkPropagation(value = MANDATORY, usecase = "Create Job")
    Job createJob(Customer customer, int quantity, LocalDate deadline, Product product);

    void refillPartsInventory(List<Job> jobs);

    class Mixin
        implements JobsService
    {
        @Structure
        UnitOfWorkFactory uowf;

        @Override
        public Job createJob(Customer customer, int quantity, LocalDate deadline, Product product)
        {
            UnitOfWork uow = uowf.currentUnitOfWork();
            EntityBuilder<Job> builder = uow.newEntityBuilder(Job.class);
            Job proto = builder.instance();
            proto.product().set(product);
            proto.quantity().set(quantity);
            proto.customer().set(customer);
            proto.deadline().set(deadline);
            return builder.newInstance();
        }

        public void refillPartsInventory(List<Job> jobs)
        {
            // TODO
        }
    }
}
