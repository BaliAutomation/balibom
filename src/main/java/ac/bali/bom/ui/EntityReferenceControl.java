package ac.bali.bom.ui;

import javafx.geometry.Pos;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import org.apache.polygene.api.composite.CompositeDescriptor;
import org.apache.polygene.api.entity.EntityReference;
import org.apache.polygene.api.injection.scope.Service;
import org.apache.polygene.api.injection.scope.Structure;
import org.apache.polygene.api.injection.scope.Uses;
import org.apache.polygene.api.object.ObjectFactory;
import org.apache.polygene.api.property.Immutable;
import org.apache.polygene.api.property.PropertyDescriptor;
import org.apache.polygene.spi.PolygeneSPI;

import java.util.Optional;

public class EntityReferenceControl extends PropertyControl<EntityReference>
{
    private final CompositePane valuePane;
    private final Label label;
    private final Hyperlink field;

    public EntityReferenceControl(@Structure ObjectFactory obf,
                                  @Structure PolygeneSPI spi,
                                  @Service PropertyCtrlFactory factory,
                                  @Uses PropertyDescriptor descriptor)
    {
        super(factory, descriptor.metaInfo(Immutable.class) != null, factory.nameOf(descriptor));
        Class<?> compositeType = descriptor.valueType().primaryType();
        valuePane = obf.newObject(CompositePane.class, compositeType, descriptor.metaInfo(Immutable.class) != null);
        field = new Hyperlink();
        label = labelOf();
        label.setPadding(PADDING);
        field.setOnAction(ev ->
        {
            CompositeDescriptor compositeDescriptor = spi.compositeDescriptorFor(value);
            CompositeDialog<Object> dialog = obf.newObject(CompositeDialog.class, compositeDescriptor, label, valuePane, true);
            Optional<Object> result = dialog.showAndWait();
            result.ifPresent(t -> fireEvent(new PropertyDataEvent(EntityReferenceControl.this, value, t)));
        });
        HBox box = wrapInHBox(label, field);
        HBox.setHgrow(field, Priority.ALWAYS);
        setAlignment(Pos.TOP_LEFT);
        setFillWidth(true);
        getChildren().add(box);
    }

    @Override
    public void clear()
    {
        field.setText("");

    }

    @Override
    protected void updateTo(EntityReference value)
    {
        field.setText(factory.nameOf(value));

    }

    @Override
    protected EntityReference currentValue()
    {
        return value;
    }
}
