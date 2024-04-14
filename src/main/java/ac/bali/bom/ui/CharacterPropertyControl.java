package ac.bali.bom.ui;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.util.converter.CharacterStringConverter;
import org.apache.polygene.api.injection.scope.Service;
import org.apache.polygene.api.injection.scope.Uses;
import org.apache.polygene.api.property.Immutable;
import org.apache.polygene.api.property.PropertyDescriptor;

public class CharacterPropertyControl extends PropertyControl<Character>
{
    private final TextInputControl field;

    public CharacterPropertyControl(@Service PropertyCtrlFactory factory, @Uses PropertyDescriptor descriptor, @Uses boolean withLabel)
    {
        super(factory, descriptor.metaInfo(Immutable.class) != null, factory.nameOf(descriptor));
        field = new TextField();
        field.setTextFormatter(new TextFormatter<>(new CharacterStringConverter()));
        Pane box;
        if (withLabel)
        {
            Label label = labelOf();
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
    protected void updateTo(Character value)
    {
        field.setText(value.toString());
    }

    protected Character currentValue()
    {
        return field.getText().charAt(0);
    }

    @Override
    public void clear()
    {
        field.setText("");
    }
}
