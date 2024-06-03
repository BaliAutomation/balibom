package ac.bali.bom.inventory.products;

import ac.bali.bom.products.Product;
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

@Mixins(ProductsInventoryService.Mixin.class)
public interface ProductsInventoryService
{
    @UnitOfWorkPropagation(MANDATORY)
    void addProduct(Product product);

    @Action(label = "Quantity Set...", scope = composite)
    @UnitOfWorkPropagation(MANDATORY)
    void quantitySet(ProductsInventory inventory, int quantity);

    @Action(label = "Quantity Added...", scope = composite)
    @UnitOfWorkPropagation(MANDATORY)
    void quantityAdded(ProductsInventory inventory, int quantity);

    class Mixin
        implements ProductsInventoryService
    {
        @Structure
        Module module;

        @Override
        public void addProduct(Product product)
        {
             inventoryOf(product);
        }

        @Override
        public void quantitySet(ProductsInventory inventory, int quantity)
        {
            inventory.quantity().set(quantity);
        }

        @Override
        public void quantityAdded(ProductsInventory inventory, int quantity)
        {
            inventory.quantity().set(inventory.quantity().get() + quantity);
        }

        private ProductsInventory inventoryOf(Product product)
        {
            UnitOfWork uow = module.unitOfWorkFactory().currentUnitOfWork();
            Identity identity = StringIdentity.identityOf("inventory/" + product.identity().toString());
            try
            {
                ProductsInventory inventory = uow.get(ProductsInventory.class, identity);
                return inventory;
            } catch (NoSuchEntityException e)
            {
                EntityBuilder<ProductsInventory> builder = uow.newEntityBuilder(ProductsInventory.class);
                ProductsInventory instance = builder.instance();
                instance.product().set(product);
                instance.quantity().set(0);
                return builder.newInstance();
            }
        }
    }
}
