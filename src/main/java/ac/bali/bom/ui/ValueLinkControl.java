package ac.bali.bom.ui;

import javafx.geometry.Pos;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import org.apache.polygene.api.composite.CompositeDescriptor;
import org.apache.polygene.api.injection.scope.Service;
import org.apache.polygene.api.injection.scope.Structure;
import org.apache.polygene.api.injection.scope.Uses;
import org.apache.polygene.api.object.ObjectFactory;
import org.apache.polygene.api.property.Immutable;
import org.apache.polygene.api.property.PropertyDescriptor;
import org.apache.polygene.spi.PolygeneSPI;

import java.util.Optional;

/**
 * ValueLinkControl shows any @RenderAsName as a link, and if clicking on the link a popup with the form of the
 * data will appear. If the {@code Property<X>} is {@code @Immutable} the values on the popup panel is not editable,
 * only viewable.
 *
 * @param <T>
 */
public class ValueLinkControl<T> extends PropertyControl<T>
{

    private final CompositePane<T> valuePane;
    private final Hyperlink field;
    private final Label label;

    @SuppressWarnings("unchecked")
    public ValueLinkControl(@Structure PolygeneSPI spi,
                            @Structure ObjectFactory obf,
                            @Service PropertyCtrlFactory factory,
                            @Uses PropertyDescriptor descriptor)
    {
        super(factory, false, factory.nameOf(descriptor));
        Class<?> compositeType = descriptor.valueType().primaryType();
        valuePane = obf.newObject(CompositePane.class, compositeType, descriptor.metaInfo(Immutable.class) != null);
        field = new Hyperlink();
        label = labelOf();
        label.setPadding(PADDING);
        field.setOnAction(ev ->
        {
            CompositeDescriptor compositeDescriptor = spi.compositeDescriptorFor(value);
            CompositeDialog<T> dialog = obf.newObject(CompositeDialog.class, compositeDescriptor, label, valuePane, false);
            Optional<T> result = dialog.showAndWait();
            result.ifPresent(t -> fireEvent(new PropertyDataEvent(ValueLinkControl.this, value, t)));
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
    protected void updateTo(T value)
    {
        field.setText(factory.nameOf(value));
    }

    @Override
    protected T currentValue()
    {
        return value;
    }


}


