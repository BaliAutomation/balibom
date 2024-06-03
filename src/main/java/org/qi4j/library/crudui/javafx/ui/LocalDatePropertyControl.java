package org.qi4j.library.crudui.javafx.ui;

import java.time.LocalDate;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Pos;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import org.apache.polygene.api.injection.scope.Service;
import org.apache.polygene.api.injection.scope.Uses;
import org.apache.polygene.api.property.PropertyDescriptor;

public class LocalDatePropertyControl extends DateTimePropertyControl<LocalDate>
{
    private final DatePicker field;
    private final javafx.beans.property.Property<LocalDate> uiProperty = new SimpleObjectProperty<>();

    public LocalDatePropertyControl(@Service PropertyCtrlFactory factory, @Uses PropertyDescriptor descriptor, @Uses Boolean withLabel)
    {
        super(factory, withLabel ? factory.nameOf(descriptor) : null);
        field = new DatePicker(LocalDate.now());
        Pane box;
        if (withLabel)
        {
            Label label = labelOf();
            box = wrapInHBox(label, field);
        } else
        {
            box = wrapInHBox(field);
        }
        field.valueProperty().bindBidirectional(uiProperty);

        HBox.setHgrow(field, Priority.ALWAYS);
        setAlignment(Pos.TOP_LEFT);
        setFillWidth(true);
        getChildren().add(box);
//        setStyle("-fx-border-style: solid; -fx-border-color: blue; -fx-border-width: 2px");
    }

    @Override
    public void clear()
    {
        super.clear();
        field.setValue(null);
    }

    @Override
    public javafx.beans.property.Property<LocalDate> uiProperty()
    {
        return uiProperty;
    }


}
