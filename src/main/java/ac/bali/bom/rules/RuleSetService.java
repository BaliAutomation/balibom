package ac.bali.bom.rules;

import ac.bali.bom.view.Deleter;
import java.util.List;
import org.apache.polygene.api.entity.EntityBuilder;
import org.apache.polygene.api.identity.Identity;
import org.apache.polygene.api.identity.StringIdentity;
import org.apache.polygene.api.injection.scope.Structure;
import org.apache.polygene.api.mixin.Mixins;
import org.apache.polygene.api.unitofwork.UnitOfWork;
import org.apache.polygene.api.unitofwork.UnitOfWorkFactory;
import org.qi4j.library.crudui.Action;
import org.qi4j.library.crudui.ActionScope;
import org.qi4j.library.crudui.HasListViewController;


@HasListViewController({RuleSetService.class,Deleter.class})
@Mixins(RuleSetService.Mixin.class)
public interface RuleSetService
{
    @Action(label="Create Rule Set...", scope= ActionScope.type)
    void create(String name, List<RuleEntity> rules);

    class Mixin implements RuleSetService
    {
        @Structure
        private UnitOfWorkFactory uowf;

        @Override
        public void create(String name, List<RuleEntity> rules)
        {
            UnitOfWork uow = uowf.currentUnitOfWork();
            Identity identity = StringIdentity.identityOf("ruleset/" + name);
            EntityBuilder<RuleSet> builder = uow.newEntityBuilder(RuleSet.class, identity);
            RuleSet instance = builder.instance();
            instance.name().set(name);
            for( RuleEntity rule: rules)
                instance.rules().add(rule);
            builder.newInstance();
        }
    }
}
