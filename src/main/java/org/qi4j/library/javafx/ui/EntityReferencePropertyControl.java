package org.qi4j.library.javafx.ui;

import java.util.Optional;
import javafx.geometry.Pos;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import org.apache.polygene.api.association.AssociationDescriptor;
import org.apache.polygene.api.composite.CompositeDescriptor;
import org.apache.polygene.api.entity.EntityReference;
import org.apache.polygene.api.injection.scope.Service;
import org.apache.polygene.api.injection.scope.Structure;
import org.apache.polygene.api.injection.scope.Uses;
import org.apache.polygene.api.object.ObjectFactory;
import org.apache.polygene.api.property.Immutable;
import org.apache.polygene.api.unitofwork.UnitOfWork;
import org.apache.polygene.api.unitofwork.UnitOfWorkFactory;
import org.apache.polygene.api.usecase.Usecase;
import org.apache.polygene.api.usecase.UsecaseBuilder;
import org.apache.polygene.spi.PolygeneSPI;

public class EntityReferencePropertyControl extends PropertyControl<EntityReference>
{
    private final Hyperlink field;
    private final Label label;

    public EntityReferencePropertyControl(@Structure ObjectFactory obf,
                                          @Structure PolygeneSPI spi,
                                          @Structure UnitOfWorkFactory uowf,
                                          @Service PropertyCtrlFactory factory,
                                          @Uses AssociationDescriptor descriptor)
    {
        super(factory, false, factory.nameOf(descriptor));
        field = new Hyperlink();
        label = labelOf();
        label.setPadding(PADDING);
        field.setOnAction(ev ->
        {
            Class<?> compositeType = (Class<?>) descriptor.type();
            CompositePane<?> valuePane = obf.newObject(CompositePane.class, compositeType, descriptor.metaInfo(Immutable.class) != null);
            Usecase usecase = UsecaseBuilder.newUsecase("View Association: " + value);
            try (UnitOfWork uow = uowf.newUnitOfWork(usecase))
            {
                Object obj = uow.get((Class<?>) descriptor.type(), value.identity());
                CompositeDescriptor compositeDescriptor = spi.entityDescriptorFor(obj);
                //noinspection unchecked
                CompositeDialog<Object> dialog = (CompositeDialog<Object>) obf.newObject(CompositeDialog.class, compositeDescriptor, label, valuePane, true);
                dialog.setValue(obj);
                Optional<Object> result = dialog.showAndWait();
                result.ifPresent(t -> fireEvent(new PropertyDataEvent(EntityReferencePropertyControl.this, value, t)));
            }
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
    protected void updateTo(EntityReference value)
    {
        if (value != null)
            field.setText(factory.nameOf(value));
    }

    @Override
    protected EntityReference currentValue()
    {
//        EntityReference ref = EntityReference.parseEntityReference(field.getText());
        return value;
    }
}
