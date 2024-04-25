package org.qi4j.library.javafx.ui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.apache.polygene.api.injection.scope.Service;
import org.apache.polygene.api.injection.scope.Structure;
import org.apache.polygene.api.injection.scope.Uses;
import org.apache.polygene.api.object.ObjectFactory;
import org.apache.polygene.api.property.Immutable;
import org.apache.polygene.api.property.PropertyDescriptor;
import org.apache.polygene.spi.PolygeneSPI;

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
            valuePane.updateWith(currentValue());
            VBox root = new VBox(valuePane);
            root.setFillWidth(true);
            VBox.setVgrow(valuePane, Priority.ALWAYS);
            Scene scene = new Scene(root,1200,800);
            Stage compositeStage = new Stage();
            compositeStage.setScene(scene);
            compositeStage.setTitle(label.getText());
            compositeStage.show();
            compositeStage.addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, evt -> {
                compositeStage.setScene(null);
                compositeStage.close();
                root.getChildren().clear();
            });
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


