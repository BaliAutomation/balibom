package ac.bali.bom.issues;

import org.apache.polygene.api.concern.Concerns;
import org.apache.polygene.api.entity.EntityBuilder;
import org.apache.polygene.api.injection.scope.Structure;
import org.apache.polygene.api.mixin.Mixins;
import org.apache.polygene.api.unitofwork.UnitOfWork;
import org.apache.polygene.api.unitofwork.UnitOfWorkFactory;
import org.apache.polygene.api.unitofwork.concern.UnitOfWorkConcern;
import org.apache.polygene.api.unitofwork.concern.UnitOfWorkPropagation;

import static org.apache.polygene.api.unitofwork.concern.UnitOfWorkPropagation.Propagation.MANDATORY;

@Mixins(IssuesService.Mixin.class)
@Concerns(UnitOfWorkConcern.class)
public interface IssuesService
{
    @UnitOfWorkPropagation(MANDATORY)
    void reportIssue(String topic, String issue);

    class Mixin
        implements IssuesService
    {
        @Structure
        UnitOfWorkFactory uowf;

        @Override
        public void reportIssue(String topic, String issue)
        {
            UnitOfWork uow = uowf.currentUnitOfWork();
            EntityBuilder<Issue> builder = uow.newEntityBuilder(Issue.class);
            builder.instance().topic().set(topic);
            builder.instance().issue().set(issue);
            builder.newInstance();
        }
    }
}
