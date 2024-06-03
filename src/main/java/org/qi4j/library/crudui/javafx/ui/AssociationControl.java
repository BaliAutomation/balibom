package org.qi4j.library.crudui.javafx.ui;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.apache.polygene.api.association.Association;
import org.apache.polygene.api.association.AssociationDescriptor;
import org.apache.polygene.api.injection.scope.Service;
import org.apache.polygene.api.injection.scope.Structure;
import org.apache.polygene.api.injection.scope.Uses;
import org.apache.polygene.api.object.ObjectFactory;
import org.apache.polygene.api.property.Immutable;
import org.qi4j.library.crudui.javafx.support.ObservableAssociationWrapper;

public class AssociationControl<T> extends PropertyControl<T>
{
    protected final PropertyCtrlFactory factory;
    private final AssociationDescriptor descriptor;
    private final Hyperlink field;
    private final Label label;
    private final CompositePane entityPane;

    private final SimpleObjectProperty<T> uiProperty = new SimpleObjectProperty<>();

    private ObservableAssociationWrapper<T> association;

    public AssociationControl(@Structure ObjectFactory obf,
                              @Service PropertyCtrlFactory factory,
                              @Uses AssociationDescriptor descriptor,
                              @Uses boolean withLabel)
    {
        super(factory, withLabel ? factory.nameOf(descriptor) : null);
        this.factory = factory;
        this.descriptor = descriptor;

        Class<?> compositeType = (Class<?>) descriptor.type();
        entityPane = obf.newObject(CompositePane.class, compositeType, descriptor.metaInfo(Immutable.class) != null);
        field = new Hyperlink();
        field.setAlignment(Pos.CENTER_RIGHT);
        field.setPadding(PADDING);
        label = labelOf();
        label.setPadding(PADDING);

        uiProperty.addListener((ObservableValue<? extends T> observable, T oldValue, T newValue) ->
        {
            if (newValue != null)
                field.setText(newValue.toString());
            else
                field.setText("");
        });

        field.setOnAction(ev ->
        {
            entityPane.updateWith(association.get());
            VBox root = new VBox(entityPane);
            root.setFillWidth(true);
            VBox.setVgrow(entityPane, Priority.ALWAYS);
            Scene scene = new Scene(root, 1200, 800);
            Stage compositeStage = new Stage();
            compositeStage.setScene(scene);
            compositeStage.setTitle(label.getText());
            compositeStage.show();
            compositeStage.addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, evt ->
            {
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
        super.clear();
        field.setText("");
    }

    @Override
    public javafx.beans.property.Property<T> uiProperty()
    {
        return uiProperty;
    }

    public void bind(Association<T> p)
    {
        unbind();
        javafx.beans.property.Property<T> value = uiProperty();
        if (value == null)
            return;
        if (p instanceof ObservableAssociationWrapper<T> assoc)
        {
            association = assoc;
            Bindings.bindBidirectional(value, assoc);
        }
    }

    private void unbind()
    {
        javafx.beans.property.Property<T> value = uiProperty();
        if (value == null)
        {
            System.err.println("bind() not supported: " + this.getClass().getSimpleName());
            return;
        }
        if (association != null)
            Bindings.unbindBidirectional(value, association);
    }
}
