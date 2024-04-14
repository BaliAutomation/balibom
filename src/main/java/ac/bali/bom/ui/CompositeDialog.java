package ac.bali.bom.ui;

import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import org.apache.polygene.api.composite.CompositeDescriptor;
import org.apache.polygene.api.injection.scope.Uses;

public class CompositeDialog<T> extends Dialog<T>
{
    private final CompositePane<T> valuePane;

    public CompositeDialog(@Uses CompositeDescriptor descriptor, @Uses Label label, @Uses CompositePane<T> valuePane, @Uses Boolean immutable)
    {
        this.valuePane = valuePane;
        initModality(Modality.APPLICATION_MODAL);
        DialogPane dialogPane = getDialogPane();
        setTitle("Content of " + label.getText());
        Button cancelButton = new Button("Cancel");
        cancelButton.isCancelButton();
        Button saveButton = new Button("OK");
        saveButton.isDefaultButton();
        saveButton.setOnAction(e -> close());
        cancelButton.setOnAction(e -> close());
        dialogPane.getButtonTypes().add(ButtonType.OK);
        dialogPane.getButtonTypes().add(ButtonType.CANCEL);
        dialogPane.lookupButton(ButtonType.OK).setVisible(false);
        dialogPane.lookupButton(ButtonType.CANCEL).setVisible(false);

        VBox content = new VBox(valuePane, new HBox(cancelButton, saveButton));
        content.setDisable(immutable);
        dialogPane.setContent(content);
    }

    public void setValue(T value)
    {
        valuePane.updateWith(value);
    }

    public T getValue()
    {
        return valuePane.toValue();
    }
}
