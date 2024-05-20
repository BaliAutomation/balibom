package ac.bali.bom.view;

import ac.bali.bom.suppliers.Supplier;
import ac.bali.bom.suppliers.manual.model.Product;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import javafx.scene.control.Label;
import org.apache.polygene.api.injection.scope.Structure;
import org.apache.polygene.api.mixin.Mixins;
import org.apache.polygene.api.object.ObjectFactory;
import org.apache.polygene.api.value.ValueBuilder;
import org.apache.polygene.api.value.ValueBuilderFactory;
import org.qi4j.library.javafx.support.FieldDescriptor;
import org.qi4j.library.javafx.ui.ParametersForm;

@Mixins({ManualProductSearch.Mixin.class})
public interface ManualProductSearch
{
    Product searchPart(Supplier supplier, String mf, String mpn, List<String> sources, Map<String, String> attributes);


    class Mixin
        implements ManualProductSearch
    {
        private final FieldDescriptor[] fields;
        @Structure
        ObjectFactory obf;

        @Structure
        ValueBuilderFactory vbf;

        private final ParametersForm form;

        public Mixin(@Structure ObjectFactory obf, @Structure ValueBuilderFactory vbf)
        {
            this.obf = obf;
            this.vbf = vbf;
            fields = new FieldDescriptor[]{
                createField("Sources", List.class, 4, vbf),
                createField("Manufacturer", String.class, vbf),
                createField("Manufacturer Part Number", String.class, vbf),
                createField("Supplier Part Number", String.class, vbf),
                createField("Part Description", String.class, vbf),
                createField("Datasheet Url", String.class, vbf),
                createField("Quantity Available", Integer.class, vbf),
                createField("Is Reel", Boolean.class, vbf),
                createField("Reel Size", Integer.class, vbf),
                createField("Minimum Purchase", Integer.class, vbf),
                createField("Prices", Map.class, 6, vbf)
            };
            form = obf.newObject(ParametersForm.class, "Manual Supply", fields);
        }

        @Override
        public Product searchPart(Supplier supplier, String mf, String mpn, List<String> sources, Map<String, String> attributes)
        {
            return askForProduct(mf, mpn);
        }

        private Product askForProduct(String mf, String mpn)
        {
            form.clear();
            form.setWidth(900);
            form.setValue("Manufacturer", mf);
            form.setValue("Manufacturer Part Number", mf);
            form.setValue("Prc", mf);
            Object[] objects = form.showAndWait().orElse(null);
            if (objects == null)
                return null;
            ValueBuilder<Product> builder = vbf.newValueBuilder(Product.class);
            Product proto = builder.prototype();
            proto.manufacturer().set(mf);
            proto.manufacturerPartNumber().set(mpn);
            proto.supplierPartNumber().set((String) objects[2]);
            proto.partDescription().set((String) objects[3]);
            proto.datasheetUrl().set((String) objects[4]);
            proto.quantityAvailable().set((Integer) objects[5]);
            proto.isReel().set((Boolean) objects[6]);
            proto.reelSize().set((Integer) objects[7]);
            proto.minimumPurchase().set((Integer) objects[8]);
            proto.prices().set((Map<Integer, BigDecimal>) objects[9]);
            return builder.newInstance();
        }

        private FieldDescriptor createField(String name, Class<?> type, ValueBuilderFactory vbf)
        {
            return createField(name, type, 1, vbf);
        }

        private static FieldDescriptor createField(String name, Class<?> type, int height, ValueBuilderFactory vbf)
        {
            Label label = new Label("Abc");
            ValueBuilder<FieldDescriptor> builder = vbf.newValueBuilder(FieldDescriptor.class);
            FieldDescriptor prototype = builder.prototype();
            prototype.name().set(name);
            prototype.type().set(type);
            prototype.height().set(height);
            prototype.width().set((int) (label.getHeight() * height));
            return builder.newInstance();
        }
    }
}