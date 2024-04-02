package ac.bali.bom.ui;

import ac.bali.bom.support.RenderAsDescription;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.HBox;
import org.apache.polygene.api.injection.scope.Service;
import org.apache.polygene.api.injection.scope.Uses;
import org.apache.polygene.api.property.PropertyDescriptor;

public class StringPropertyControl extends PropertyControl<String>
{
    private final TextInputControl field;

    public StringPropertyControl(@Service PropertyCtrlFactory factory,
                                 @Uses PropertyDescriptor descriptor,
                                 @Uses Boolean withLabel)
    {
        super(factory, descriptor.isImmutable(), factory.nameOf(descriptor));
        if (descriptor.metaInfo(RenderAsDescription.class) == null)
        {
            field = new TextField();
        } else
        {
            field = new TextArea();
        }
//        field.setPadding(PADDING);
        HBox box;
        if (withLabel)
        {
            Label label = labelOf();
            box = wrapInHBox(label, field);
        } else
        {
            box = wrapInHBox(field);
        }
        getChildren().add(box);
    }

    @Override
    protected void updateTo(String value)
    {
        field.setText(value.toString());
    }

    protected String currentValue() {
        return field.getText();
    }

    @Override
    public void clear()
    {
        field.setText("");
    }
}
