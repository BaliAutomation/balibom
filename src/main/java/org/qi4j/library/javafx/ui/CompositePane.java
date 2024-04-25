package org.qi4j.library.javafx.ui;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.apache.polygene.api.association.AssociationDescriptor;
import org.apache.polygene.api.association.AssociationStateDescriptor;
import org.apache.polygene.api.association.AssociationStateHolder;
import org.apache.polygene.api.entity.EntityComposite;
import org.apache.polygene.api.entity.EntityDescriptor;
import org.apache.polygene.api.entity.EntityReference;
import org.apache.polygene.api.identity.Identity;
import org.apache.polygene.api.injection.scope.Service;
import org.apache.polygene.api.injection.scope.Structure;
import org.apache.polygene.api.injection.scope.Uses;
import org.apache.polygene.api.mixin.Initializable;
import org.apache.polygene.api.property.PropertyDescriptor;
import org.apache.polygene.api.structure.MetaInfoHolder;
import org.apache.polygene.api.unitofwork.UnitOfWork;
import org.apache.polygene.api.unitofwork.UnitOfWorkFactory;
import org.apache.polygene.api.usecase.Usecase;
import org.apache.polygene.api.usecase.UsecaseBuilder;
import org.apache.polygene.api.value.ValueBuilderFactory;
import org.apache.polygene.api.value.ValueComposite;
import org.apache.polygene.api.value.ValueDescriptor;
import org.apache.polygene.spi.PolygeneSPI;
import org.apache.polygene.spi.module.ModuleSpi;
import org.qi4j.library.javafx.support.Ignore;
import org.qi4j.library.javafx.support.MemberOrderComparator;

public class CompositePane<T> extends VBox
    implements Initializable
{
    private final Map<String, PropertyControl<?>> controls = new HashMap<>();

    @Uses
    private Class<T> compositeType;

    @Uses
    Boolean immutable;

    @Service
    PropertyCtrlFactory factory;

    @Structure
    PolygeneSPI spi;

    @Structure
    ValueBuilderFactory vbf;

    @Structure
    UnitOfWorkFactory uowf;

    @Structure
    ModuleSpi module;

    T currentValue;

    @SuppressWarnings("unchecked")
    public void updateWith(T newValue)
    {
        T oldValue = currentValue;
        AssociationStateHolder state;
        if (newValue instanceof ValueComposite)
            state = spi.stateOf((ValueComposite) newValue);
        else if (newValue instanceof EntityComposite)
            state = spi.stateOf((EntityComposite) newValue);
        else
            return;
        AssociationStateDescriptor compositeState = module.typeLookup().lookupValueModel(compositeType).state();
        compositeState.properties().forEach(p ->
        {
            String name = factory.nameOf(p);
            PropertyControl<Object> ctrl = (PropertyControl<Object>) controls.get(name);
            if (ctrl != null)
            {
                Object value = state.propertyFor(p.accessor()).get();
                ctrl.setValue(value);
            }
        });
        compositeState.associations().forEach(assoc ->
        {
            String name = factory.nameOf(assoc);
            PropertyControl<Object> ctrl = (PropertyControl<Object>) controls.get(name);
            if (ctrl != null)
            {
                Usecase usecase = UsecaseBuilder.newUsecase("Dereference " + assoc.qualifiedName());
                //noinspection unused
                try (UnitOfWork uow = uowf.newUnitOfWork(usecase))
                {
                    Object value = state.associationFor(assoc.accessor()).reference();
                    ctrl.setValue(value);
                }
            }
        });
        fireEvent(new DataEvent<T>(this, oldValue, newValue));
    }

    public void clearForm()
    {
        controls.values().forEach(PropertyControl::clear);
    }

    private Node createForm()
    {
        this.controls.clear();
        VBox vbox = new VBox();
        ValueDescriptor valueDescriptor = module.typeLookup().lookupValueModel(compositeType);
        AssociationStateDescriptor state;
        state = getState(valueDescriptor);
        if (state == null)
            throw new IllegalArgumentException(compositeType.getName() + " is not a ValueComposite or EntityComposite.");

        List<MetaInfoHolder> members = new ArrayList<>();
        List<PropertyDescriptor> descs = state.properties().collect(Collectors.toList());
        List<AssociationDescriptor> assocs = state.associations().collect(Collectors.toList());
        List<AssociationDescriptor> many = state.manyAssociations().collect(Collectors.toList());
        List<AssociationDescriptor> named = state.namedAssociations().collect(Collectors.toList());
        members.addAll(descs);
        members.addAll(assocs);
        members.addAll(named);
        members.addAll(many);
        members.stream()
            .filter(property -> property.metaInfo(Ignore.class) == null)
            .sorted(new MemberOrderComparator())
            .map(member ->
            {
                switch( member.getClass().getSimpleName() )
                {
                    case "PropertyModel":
                    {
                        PropertyDescriptor property = (PropertyDescriptor) member;
                        PropertyControl<?> control = factory.createPropertyControl(property, true);
                        if (control == null)
                            return null;
                        return new SimpleEntry<>(factory.nameOf(property), control);
                    }
                    case "AssociationModel":
                    {
                        AssociationDescriptor assoc = (AssociationDescriptor) member;
                        PropertyControl<?> control = factory.createAssociationControl(assoc, true);
                        if (control == null)
                            return null;
                        return new SimpleEntry<>(factory.nameOf(assoc), control);
                    }
                    case "ManyAssociationModel":
                    {
                        AssociationDescriptor assoc = (AssociationDescriptor) member;
                        PropertyControl<?> control = factory.createManyAssociationControl(assoc, true);
                        if (control == null)
                            return null;
                        return new SimpleEntry<>(factory.nameOf(assoc), control);
                    }
                    case "NamedAssociationModel":
                    {
                        AssociationDescriptor assoc = (AssociationDescriptor) member;
                        PropertyControl<?> control = factory.createNamedAssociationControl(assoc, true);
                        if (control == null)
                            return null;
                        return new SimpleEntry<>(factory.nameOf(assoc), control);
                    }
                    default:
                        return null;
                }
            })
            .filter(Objects::nonNull)
            .forEach(entry ->
            {
                PropertyControl<?> valueCtrl = entry.getValue();
                vbox.getChildren().add(valueCtrl);
                this.controls.put(entry.getKey(), valueCtrl);
            });
        vbox.setFillWidth(true);
//        vbox.setStyle("-fx-border-color: blue;");
        return vbox;
    }

    private AssociationStateDescriptor getState(ValueDescriptor valueDescriptor)
    {
        AssociationStateDescriptor state;
        if (valueDescriptor != null)
        {
            state = valueDescriptor.state();
        } else
        {
            EntityDescriptor entityDescriptor = module.typeLookup().lookupEntityModel(compositeType);
            if (entityDescriptor != null)
            {
                state = entityDescriptor.state();
            } else
            {
                return null;
            }
        }
        return state;
    }

    @Override
    public void initialize() throws Exception
    {
        setFillWidth(true);
        Node form = createForm();
        ScrollPane scroll = new ScrollPane(form);
        scroll.setFitToWidth(true);
        scroll.setFitToHeight(true);
        HBox.setHgrow(scroll, Priority.ALWAYS);
        HBox box = new HBox(scroll);
        VBox.setVgrow(box, Priority.ALWAYS);
        box.setFillHeight(true);
        getChildren().add(box);
//        box.setStyle("-fx-border-color: green;");
        VBox.setVgrow(form, Priority.ALWAYS);
//        setStyle("-fx-border-color: red;");
    }

    public T toValue()
    {
        //noinspection UnnecessaryLocalVariable
        T value = vbf.newValueBuilderWithState(
            compositeType,
            p ->
            {
                PropertyControl<?> control = controls.get(factory.nameOf(p));
                return control.valueOf();
            },
            a ->
            {
                PropertyControl<?> control = controls.get(factory.nameOf(a));
                Object v = control.valueOf();
                if (v instanceof String)
                    return EntityReference.parseEntityReference((String) v);
                else if (v instanceof Identity)
                    return EntityReference.create((Identity) v);
                else
                    throw new InternalError("Associations should be serialized to strings");
            },
            m -> null,
            n -> null
        ).newInstance();
        return value;
    }

    private static class DataEvent<T> extends Event
    {
        private static final EventType<DataEvent<?>> COMPOSITE_EVENT = new EventType<>(EventType.ROOT, "COMPOSITEPANE_DATA_EVENT");
        private final T oldValue;
        private final T newValue;

        public DataEvent(CompositePane<T> pane, T oldValue, T newValue)
        {
            super(pane, pane, COMPOSITE_EVENT);
            this.oldValue = oldValue;
            this.newValue = newValue;
        }

        public T getOldValue()
        {
            return oldValue;
        }

        public T getNewValue()
        {
            return newValue;
        }
    }
}
