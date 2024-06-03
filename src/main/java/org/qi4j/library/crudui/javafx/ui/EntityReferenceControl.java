package org.qi4j.library.crudui.javafx.ui;

import javafx.beans.property.Property;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.apache.polygene.api.association.AssociationDescriptor;
import org.apache.polygene.api.entity.EntityReference;
import org.apache.polygene.api.injection.scope.Service;
import org.apache.polygene.api.injection.scope.Structure;
import org.apache.polygene.api.injection.scope.Uses;
import org.apache.polygene.api.object.ObjectFactory;
import org.apache.polygene.api.property.Immutable;
import org.apache.polygene.api.unitofwork.UnitOfWork;
import org.apache.polygene.api.unitofwork.UnitOfWorkFactory;
import org.apache.polygene.spi.PolygeneSPI;

public class EntityReferenceControl extends PropertyControl<EntityReference>
{
    private final Hyperlink field;
    private final Label label;
    private final CompositePane<Object> valuePane;

    public EntityReferenceControl(@Structure ObjectFactory obf,
                                  @Structure PolygeneSPI spi,
                                  @Structure UnitOfWorkFactory uowf,
                                  @Service PropertyCtrlFactory factory,
                                  @Uses AssociationDescriptor descriptor)
    {
        super(factory, factory.nameOf(descriptor));
        field = new Hyperlink();
        field.setAlignment(Pos.CENTER_RIGHT);
        field.setPadding(PADDING);
        label = labelOf();
        label.setPadding(PADDING);
        Class<?> compositeType = (Class<?>) descriptor.type();
        UnitOfWork uow = uowf.currentUnitOfWork();
        //noinspection unchecked
        valuePane = obf.newObject(CompositePane.class, compositeType, descriptor.metaInfo(Immutable.class) != null);
        field.setOnAction(ev ->
        {
            Object obj = uow.get((Class<?>) descriptor.type(), currentValue().identity());
            valuePane.updateWith(obj);
            VBox root = new VBox(valuePane);
            root.setFillWidth(true);
            setVgrow(valuePane, Priority.ALWAYS);
            Scene scene = new Scene(root, 1200, 800);
            Stage compositeStage = new Stage();
            compositeStage.setTitle(label.getText());
            compositeStage.setScene(scene);
            compositeStage.addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, evt ->
            {
                compositeStage.setScene(null);
                compositeStage.close();
                root.getChildren().clear();
            });
            compositeStage.show();
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
    public Property<EntityReference> uiProperty()
    {
        return null;
    }
}
