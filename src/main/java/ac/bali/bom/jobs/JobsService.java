package ac.bali.bom.jobs;

import ac.bali.bom.customers.Customer;
import ac.bali.bom.inventory.parts.PartsInventoryService;
import ac.bali.bom.order.OrderService;
import ac.bali.bom.parts.Part;
import ac.bali.bom.products.Product;
import ac.bali.bom.rules.RuleSet;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.polygene.api.entity.EntityBuilder;
import org.apache.polygene.api.injection.scope.Service;
import org.apache.polygene.api.injection.scope.Structure;
import org.apache.polygene.api.mixin.Mixins;
import org.apache.polygene.api.query.Query;
import org.apache.polygene.api.query.QueryBuilder;
import org.apache.polygene.api.query.QueryBuilderFactory;
import org.apache.polygene.api.unitofwork.UnitOfWork;
import org.apache.polygene.api.unitofwork.UnitOfWorkFactory;
import org.apache.polygene.api.unitofwork.concern.UnitOfWorkPropagation;
import org.qi4j.library.crudui.Action;

import static java.util.stream.Collectors.summingInt;
import static org.apache.polygene.api.query.QueryExpressions.eq;
import static org.apache.polygene.api.query.QueryExpressions.templateFor;
import static org.apache.polygene.api.unitofwork.concern.UnitOfWorkPropagation.Propagation.MANDATORY;
import static org.qi4j.library.crudui.ActionScope.composite;

@SuppressWarnings("unused")
@Mixins(JobsService.Mixin.class)
public interface JobsService
{
    @Action(label = "Create Job...")
    @UnitOfWorkPropagation(value = MANDATORY)
    Job createJob(Customer customer, int quantity, LocalDate deadline, Product product);

    @Action(label = "Mark to be Ordered", scope = composite)
    @UnitOfWorkPropagation(value = MANDATORY)
    void markToBeOrdered(List<Job> jobs);

    @Action(label = "Create Orders...", scope = composite)
    @UnitOfWorkPropagation(value = MANDATORY)
    void createOrders(List<Job> jobs, RuleSet ruleSet);

    @Action(label = "Start", scope = composite)
    @UnitOfWorkPropagation(value = MANDATORY)
    void jobsStarted(List<Job> jobs, RuleSet ruleSet);

    @Action(label = "Completed", scope = composite)
    @UnitOfWorkPropagation(value = MANDATORY)
    void jobsCompleted(List<Job> jobs, RuleSet ruleSet);

    class Mixin
        implements JobsService
    {
        @Structure
        UnitOfWorkFactory uowf;

        @Service
        PartsInventoryService partsInventoryService;

        @Service
        OrderService orderService;

        @Service
        private QueryBuilderFactory qbf;

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

        @Override
        public void markToBeOrdered(List<Job> jobs)
        {
            for (Job job : jobs)
            {
                if (job.state().get() == JobState.initial)
                    job.state().set(JobState.ordering);
                else
                    System.err.println("Job [" + job + "] is not in initial state");
            }
        }

        @Override
        public void jobsStarted(List<Job> jobs, RuleSet ruleSet)
        {
            for (Job job : jobs)
            {
                job.state().set(JobState.inProgress);
            }
        }

        @Override
        public void jobsCompleted(List<Job> jobs, RuleSet ruleSet)
        {
            for (Job job : jobs)
            {
                job.state().set(JobState.completed);
            }
        }

        @Override
        public void createOrders(List<Job> jobs, RuleSet ruleSet)
        {
            UnitOfWork uow = uowf.currentUnitOfWork();
            QueryBuilder<Job> qb = qbf.newQueryBuilder(Job.class);
            Job template = templateFor(Job.class);
            qb = qb.where(eq(template.state(), JobState.ordering));
            Query<Job> query = uow.newQuery(qb);

            Map<Part, Integer> totalParts = query.stream()
                .flatMap(j -> j.product().get().parts().get().stream())
                .collect(Collectors.groupingBy(pq -> pq.part().get(), summingInt(pq -> pq.quantity().get())));
            orderService.createOrders(totalParts, ruleSet);

            query = uow.newQuery(qb);
            query.forEach( job -> job.state().set(JobState.ordered));
        }
    }
}
