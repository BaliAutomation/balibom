package org.qi4j.library.javafx.ui;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.apache.polygene.api.association.Association;
import org.apache.polygene.api.association.AssociationDescriptor;
import org.apache.polygene.api.association.AssociationStateDescriptor;
import org.apache.polygene.api.association.AssociationStateHolder;
import org.apache.polygene.api.association.ManyAssociation;
import org.apache.polygene.api.association.NamedAssociation;
import org.apache.polygene.api.entity.EntityComposite;
import org.apache.polygene.api.entity.EntityDescriptor;
import org.apache.polygene.api.injection.scope.Service;
import org.apache.polygene.api.injection.scope.Structure;
import org.apache.polygene.api.injection.scope.Uses;
import org.apache.polygene.api.mixin.Initializable;
import org.apache.polygene.api.property.Property;
import org.apache.polygene.api.property.PropertyDescriptor;
import org.apache.polygene.api.structure.MetaInfoHolder;
import org.apache.polygene.api.unitofwork.UnitOfWorkFactory;
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
    private final Map<AssociationStateDescriptor, Map<String, PropertyControl<?>>> properties = new HashMap<>();
    private final Map<AssociationStateDescriptor,Map<String, AssociationControl<?>>> associations = new HashMap<>();
    private final Map<String, ManyAssociationControl<?>> manyAssociations = new HashMap<>();
    private final Map<String, NamedAssociationControl<?>> namedAssociations = new HashMap<>();

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
    private final HashMap<AssociationStateDescriptor, HBox> forms = new HashMap<>();

    @SuppressWarnings("unchecked")
    public void updateWith(T newValue)
    {
        AssociationStateHolder state;
        AssociationStateDescriptor associationStateDescriptor;
        getChildren().clear();
        if (newValue instanceof ValueComposite)
        {
            state = spi.stateOf((ValueComposite) newValue);
            associationStateDescriptor = spi.valueDescriptorFor(newValue).state();
        }
        else if (newValue instanceof EntityComposite)
        {
            state = spi.stateOf((EntityComposite) newValue);
            associationStateDescriptor = spi.entityDescriptorFor(newValue).state();
        }
        else
        {
            return;
        }
        HBox hBox = forms.get(associationStateDescriptor);
        getChildren().add(hBox);
        state.properties().forEach(p ->
        {
            PropertyDescriptor descriptor = spi.propertyDescriptorFor(p);
            try
            {
                String name = factory.nameOf(descriptor);
                Property<?> wrapped = (Property<?>) ((Method) descriptor.accessor()).invoke(newValue);
                Map<String, PropertyControl<?>> properties = this.properties.get(associationStateDescriptor);
                //noinspection rawtypes
                PropertyControl ctrl = properties.get(name);
                ctrl.bind(wrapped);
            } catch (IllegalAccessException | InvocationTargetException e)
            {
                throw new RuntimeException("Problem in " + descriptor, e);
            }
        });
        state.allAssociations().forEach(p ->
        {
            AssociationDescriptor descriptor = spi.associationDescriptorFor(p);
            try
            {
                String name = factory.nameOf(descriptor);
                Association<?> wrapped = (Association<?>) ((Method) descriptor.accessor()).invoke(newValue);
                Map<String, AssociationControl<?>> associations = this.associations.get(associationStateDescriptor);
                //noinspection rawtypes
                AssociationControl ctrl = associations.get(name);
                ctrl.bind(wrapped);
            } catch (IllegalAccessException | InvocationTargetException e)
            {
                throw new RuntimeException("Problem in " + descriptor, e);
            }
        });
        state.allManyAssociations().forEach(p ->
        {
            AssociationDescriptor descriptor = spi.associationDescriptorFor(p);
            try
            {
                String name = factory.nameOf(descriptor);
                ManyAssociation<?> wrapped = (ManyAssociation<?>) ((Method) descriptor.accessor()).invoke(newValue);

                //noinspection rawtypes
                ManyAssociationControl ctrl = manyAssociations.get(name);
                ctrl.bind(wrapped);
            } catch (IllegalAccessException | InvocationTargetException e)
            {
                throw new RuntimeException("Problem in " + descriptor, e);
            }
        });
        state.allNamedAssociations().forEach(p ->
        {
            AssociationDescriptor descriptor = spi.associationDescriptorFor(p);
            try
            {
            String name = factory.nameOf(descriptor);
            NamedAssociation<?> wrapped = (NamedAssociation<?>) ((Method) descriptor.accessor()).invoke(newValue);

            //noinspection rawtypes
            NamedAssociationControl ctrl = namedAssociations.get(name);
            ctrl.load(wrapped);
            } catch (IllegalAccessException | InvocationTargetException e)
            {
                throw new RuntimeException("Problem in " + descriptor, e);
            }
        });
    }

    public void clearForm()
    {
        properties.values().stream().flatMap(m -> m.values().stream()).forEach(PropertyControl::clear);
        associations.values().stream().flatMap(m -> m.values().stream()).forEach(AssociationControl::clear);
        manyAssociations.values().forEach(ManyAssociationControl::clear);
        namedAssociations.values().forEach(NamedAssociationControl::clear);
        currentValue = null;
    }

    private Map<AssociationStateDescriptor, Node> createForms()
    {
        Map<AssociationStateDescriptor, Node> result = new HashMap<>();

        this.properties.clear();
        this.associations.clear();
        this.manyAssociations.clear();
        this.namedAssociations.clear();

        List<AssociationStateDescriptor> stateDescriptors = getState();
        if (stateDescriptors.size() == 0)
        {
            throw new IllegalArgumentException(compositeType.getName() + " is not a ValueComposite or EntityComposite.");
        }

        for( AssociationStateDescriptor state : stateDescriptors)
        {
            VBox vbox = new VBox();
            Map<String, PropertyControl<?>> properties = new HashMap<>();
            Map<String, AssociationControl<?>> associations = new HashMap<>();
            this.properties.put(state, properties);
            this.associations.put(state, associations);

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
                .forEach(member ->
                {
                    switch (member.getClass().getSimpleName())
                    {
                        case "PropertyModel" ->
                        {
                            PropertyDescriptor property = (PropertyDescriptor) member;
                            PropertyControl<?> control = factory.createPropertyControl(property, true);
                            if (control != null)
                            {
                                String name = factory.nameOf(property);
                                vbox.getChildren().add(control);
                                properties.put(name, control);
                            }
                        }
                        case "AssociationModel" ->
                        {
                            AssociationDescriptor assoc = (AssociationDescriptor) member;
                            AssociationControl<?> control = factory.createAssociationControl(assoc, true);
                            if (control != null)
                            {
                                String name = factory.nameOf(assoc);
                                vbox.getChildren().add(control);
                                associations.put(name, control);
                            }
                        }
                        case "ManyAssociationModel" ->
                        {
                            AssociationDescriptor assoc = (AssociationDescriptor) member;
                            ManyAssociationControl<?> control = factory.createManyAssociationControl(assoc, true);
                            if (control != null)
                            {
                                String name = factory.nameOf(assoc);
                                vbox.getChildren().add(control);
                                this.manyAssociations.put(name, control);
                            }
                        }
                        case "NamedAssociationModel" ->
                        {
                            AssociationDescriptor assoc = (AssociationDescriptor) member;
                            NamedAssociationControl<?> control = factory.createNamedAssociationControl(assoc, true);
                            if (control != null)
                            {
                                String name = factory.nameOf(assoc);
                                vbox.getChildren().add(control);
                                this.namedAssociations.put(name, control);
                            }
                        }
                    }
                });
            vbox.setFillWidth(true);
//        vbox.setStyle("-fx-border-color: blue;");
            result.put(state, vbox);
        }
        return result;
    }

    private List<AssociationStateDescriptor> getState()
    {
        List<AssociationStateDescriptor> result = new ArrayList<>();
        List<ValueDescriptor> valueDescriptors = module.typeLookup()
            .allValues()
            .filter(type -> compositeType.isAssignableFrom(type.primaryType()))
            .toList();
        if (valueDescriptors.size() > 0)
        {
            for (ValueDescriptor desc : valueDescriptors)
                result.add(desc.state());
        } else
        {
            List<EntityDescriptor> entityDescriptors = module.typeLookup()
                .allEntities()
                .filter(type -> compositeType.isAssignableFrom(type.primaryType()))
                .toList();
            for( EntityDescriptor desc: entityDescriptors)
            {
                result.add(desc.state());
            }
        }
        return result;
    }

    @Override
    public void initialize() throws Exception
    {
        setFillWidth(true);
        this.forms.clear();
        Map<AssociationStateDescriptor, Node> forms = createForms();
        for( Map.Entry<AssociationStateDescriptor, Node> entry : forms.entrySet() )
        {
            Node form = entry.getValue();
            ScrollPane scroll = new ScrollPane(form);
            scroll.setFitToWidth(true);
            scroll.setFitToHeight(true);
            HBox.setHgrow(scroll, Priority.ALWAYS);
            HBox box = new HBox(scroll);
            VBox.setVgrow(box, Priority.ALWAYS);
            box.setFillHeight(true);
//        box.setStyle("-fx-border-color: green;");
            VBox.setVgrow(form, Priority.ALWAYS);
//        setStyle("-fx-border-color: red;");
            this.forms.put(entry.getKey(), box);
        }
    }
}
