package ac.bali.bom.customers;

import org.apache.polygene.api.entity.EntityBuilder;
import org.apache.polygene.api.identity.Identity;
import org.apache.polygene.api.identity.StringIdentity;
import org.apache.polygene.api.injection.scope.Structure;
import org.apache.polygene.api.mixin.Mixins;
import org.apache.polygene.api.unitofwork.UnitOfWork;
import org.apache.polygene.api.unitofwork.UnitOfWorkFactory;
import org.apache.polygene.api.unitofwork.concern.UnitOfWorkPropagation;
import org.qi4j.library.javafx.support.Action;

import static org.apache.polygene.api.unitofwork.concern.UnitOfWorkPropagation.Propagation.MANDATORY;

@Mixins(CustomerService.Mixin.class)
public interface CustomerService
{
    @Action(label = "Create Customer...")
    @UnitOfWorkPropagation(value = MANDATORY, usecase = "Create Customer")
    Customer createCustomer(String name);

    class Mixin implements CustomerService
    {
        @Structure
        UnitOfWorkFactory uowf;

        @Override
        public Customer createCustomer(String name)
        {
            UnitOfWork uow = uowf.currentUnitOfWork();
            Identity identity = StringIdentity.identityOf("cust_" + name);
            EntityBuilder<Customer> builder = uow.newEntityBuilder(Customer.class, identity);
            builder.instance().name().set(name);
            return builder.newInstance();
        }
    }
}
