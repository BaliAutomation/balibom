package org.qi4j.library.javafx.ui;

import java.io.File;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Control;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.FileChooser;
import org.apache.polygene.api.composite.Composite;

public class ParametersForm extends Dialog<Object[]>
{
    private final GridPane grid;
    Control[] controls;

    public ParametersForm(String title, Parameter... parameters)
    {
        final DialogPane dialogPane = getDialogPane();


        this.grid = new GridPane();
        this.grid.setHgap(10);
        this.grid.setMaxWidth(Double.MAX_VALUE);
        this.grid.setAlignment(Pos.CENTER_LEFT);

        dialogPane.contentTextProperty().addListener(o -> updateGrid(parameters, dialogPane));

        setTitle(title);
        dialogPane.setHeaderText("");
        dialogPane.getStyleClass().add("text-input-dialog");
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        updateGrid(parameters, dialogPane);

        setResultConverter((dialogButton) ->
        {
            ButtonBar.ButtonData data = dialogButton == null ? null : dialogButton.getButtonData();
            if (data == ButtonBar.ButtonData.OK_DONE)
            {
                return updateArguments();
            }
            return null;
        });
    }

    private void updateGrid(Parameter[] parameters, DialogPane dialogPane)
    {
        grid.getChildren().clear();
        int[] index = new int[1];
        Arrays.stream(parameters).forEach(p ->
        {
            Class<?> parameterType = p.getType();
            Region field = selectControl(parameterType, index[0]);
            field.setMaxWidth(Double.MAX_VALUE);
            GridPane.setHgrow(field, Priority.ALWAYS);
            GridPane.setFillWidth(field, true);
            Label label = createContentLabel(dialogPane.getContentText());
            label.setPrefWidth(Region.USE_COMPUTED_SIZE);
            label.textProperty().bind(dialogPane.contentTextProperty());

            grid.add(label, 0, index[0]);
            grid.add(field, 1, index[0]);
            index[0] += 1;
        });
        getDialogPane().setContent(grid);
        Platform.runLater(() -> controls[0].requestFocus());
    }

    private Region selectControl(Class<?> type, int index)
    {
        if (type.equals(String.class))
        {
            TextField tf = new TextField();
            tf.setUserData(index);
            return tf;
        } else if (type.equals(File.class))
        {
            TextField tf = new TextField();
            tf.setUserData(index);
            Button button = new Button("...");
            button.setOnAction(evt ->
            {
                FileChooser fileChooser = new FileChooser();
                File f = fileChooser.showOpenDialog(button.getScene().getWindow());
                tf.setText(f.getAbsolutePath());
            });
            return new HBox(tf, button);
        } else if (Composite.class.isAssignableFrom(type))      // TODO: doesn't work, since interface doesn't inherits the Composite type.
        {
            Label label = new Label();
            label.setUserData(index);
            return label;
        }
        throw new IllegalArgumentException("Does not support arguments of type " + type.getName());
    }

    static Label createContentLabel(String text)
    {
        Label label = new Label(text);
        label.setMaxWidth(Double.MAX_VALUE);
        label.setMaxHeight(Double.MAX_VALUE);
        label.getStyleClass().add("content");
        label.setWrapText(true);
        label.setPrefWidth(360);
        return label;
    }

    private Object[] updateArguments()
    {
        return new Object[0];
    }
}