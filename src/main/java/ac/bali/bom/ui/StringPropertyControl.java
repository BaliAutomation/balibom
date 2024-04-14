package ac.bali.bom.ui;

import ac.bali.bom.ui.support.RenderAsDescription;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import org.apache.polygene.api.injection.scope.Service;
import org.apache.polygene.api.injection.scope.Uses;
import org.apache.polygene.api.property.Immutable;
import org.apache.polygene.api.property.PropertyDescriptor;

public class StringPropertyControl extends PropertyControl<String>
{
    private final TextInputControl field;

    public StringPropertyControl(@Service PropertyCtrlFactory factory,
                                 @Uses PropertyDescriptor descriptor,
                                 @Uses Boolean withLabel)
    {
        super(factory, descriptor.metaInfo(Immutable.class) != null, factory.nameOf(descriptor));
        if (descriptor.metaInfo(RenderAsDescription.class) == null)
        {
            field = new TextField();
        } else
        {
            TextArea ta = new TextArea();
            ta.setWrapText(true);
            field = ta;
        }
        Pane box;
        if (withLabel)
        {
            Label label = labelOf();
            if (field instanceof TextArea)
            {
                label.setAlignment(Pos.CENTER_LEFT);
                box = wrapInVBox(label, field);
            } else
                box = wrapInHBox(label, field);
        } else
        {
            box = wrapInHBox(field);
        }
        HBox.setHgrow(field, Priority.ALWAYS);
        setAlignment(Pos.TOP_LEFT);
        setFillWidth(true);
        getChildren().add(box);
//        setStyle("-fx-border-style: solid; -fx-border-color: blue; -fx-border-width: 2px");

    }

    @Override
    protected void updateTo(String value)
    {
        field.setText(value.toString());
    }

    protected String currentValue()
    {
        return field.getText();
    }

    @Override
    public void clear()
    {
        field.setText("");
    }
}
