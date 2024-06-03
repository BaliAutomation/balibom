package ac.bali.bom.inventory.parts;

import ac.bali.bom.parts.Part;
import java.util.List;
import org.apache.polygene.api.entity.EntityBuilder;
import org.apache.polygene.api.identity.Identity;
import org.apache.polygene.api.identity.StringIdentity;
import org.apache.polygene.api.injection.scope.Structure;
import org.apache.polygene.api.mixin.Mixins;
import org.apache.polygene.api.structure.Module;
import org.apache.polygene.api.unitofwork.NoSuchEntityException;
import org.apache.polygene.api.unitofwork.UnitOfWork;
import org.apache.polygene.api.unitofwork.concern.UnitOfWorkPropagation;
import org.qi4j.library.crudui.Action;

import static org.apache.polygene.api.unitofwork.concern.UnitOfWorkPropagation.Propagation.MANDATORY;
import static org.qi4j.library.crudui.ActionScope.composite;
import static org.qi4j.library.crudui.ActionScope.type;

@Mixins(PartsInventoryService.Mixin.class)
public interface PartsInventoryService
{
    @Action(label = "Pull...", scope = type)
    @UnitOfWorkPropagation(MANDATORY)
    void linkParts(List<Part> parts);

    @Action(label = "Quantity Set...", scope = composite)
    @UnitOfWorkPropagation(MANDATORY)
    void quantitySet(PartsInventory inventory, int quantity);

    @Action(label = "Quantity Added...", scope = composite)
    @UnitOfWorkPropagation(MANDATORY)
    void quantityAdded(PartsInventory inventory, int quantity);

    PartsInventory findInventoryOf(Part part);

    class Mixin
        implements PartsInventoryService
    {
        @Structure
        Module module;

        @Override
        public void linkParts(List<Part> parts)
        {
            for (Part part : parts)
                inventoryOf(part);
        }

        @Override
        public void quantityAdded(PartsInventory inventory, int quantity)
        {
            inventory.quantity().set(inventory.quantity().get() + quantity);
        }

        @Override
        public PartsInventory findInventoryOf(Part part)
        {
            return inventoryOf(part);
        }

        @Override
        public void quantitySet(PartsInventory inventory, int quantity)
        {
            inventory.quantity().set(quantity);
        }

        private PartsInventory inventoryOf(Part part)
        {
            UnitOfWork uow = module.unitOfWorkFactory().currentUnitOfWork();
            Identity identity = StringIdentity.identityOf("inventory/" + part.identity().toString());
            try
            {
                PartsInventory inventory = uow.get(PartsInventory.class, identity);
                return inventory;
            } catch (NoSuchEntityException e)
            {
                EntityBuilder<PartsInventory> builder = uow.newEntityBuilder(PartsInventory.class);
                PartsInventory instance = builder.instance();
                instance.part().set(part);
                instance.quantity().set(0);
                return builder.newInstance();
            }
        }
    }
}
