package org.qi4j.library.javafx.ui;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.polygene.api.association.Association;
import org.apache.polygene.api.association.AssociationDescriptor;
import org.apache.polygene.api.association.AssociationStateHolder;
import org.apache.polygene.api.common.Optional;
import org.apache.polygene.api.composite.ModelDescriptor;
import org.apache.polygene.api.composite.StatefulAssociationCompositeDescriptor;
import org.apache.polygene.api.entity.EntityComposite;
import org.apache.polygene.api.injection.scope.Structure;
import org.apache.polygene.api.mixin.Mixins;
import org.apache.polygene.api.object.ObjectFactory;
import org.apache.polygene.api.property.Property;
import org.apache.polygene.api.property.PropertyDescriptor;
import org.apache.polygene.api.structure.MetaInfoHolder;
import org.apache.polygene.api.type.ArrayType;
import org.apache.polygene.api.type.CollectionType;
import org.apache.polygene.api.type.EnumType;
import org.apache.polygene.api.type.MapType;
import org.apache.polygene.api.type.StatefulAssociationValueType;
import org.apache.polygene.api.type.ValueType;
import org.apache.polygene.api.value.ValueComposite;
import org.apache.polygene.api.value.ValueDescriptor;
import org.apache.polygene.spi.PolygeneSPI;
import org.apache.polygene.spi.module.ModuleSpi;
import org.qi4j.library.javafx.support.MemberOrderComparator;
import org.qi4j.library.javafx.support.RenderAsName;
import org.qi4j.library.javafx.support.RenderAsValue;

@Mixins(PropertyCtrlFactory.Mixin.class)
public interface PropertyCtrlFactory
{
    PropertyControl<?> createPropertyControl(PropertyDescriptor descriptor, boolean withLabel);

    AssociationControl<?> createAssociationControl(AssociationDescriptor descriptor, boolean withLabel);

    ManyAssociationControl<?> createManyAssociationControl(AssociationDescriptor descriptor, boolean withLabel);

    NamedAssociationControl<?> createNamedAssociationControl(AssociationDescriptor descriptor, boolean withLabel);

    void registerPropertyControlFactory(ValueType type, BiFunction<PropertyDescriptor, Boolean, PropertyControl<?>> factory);

    String nameOf(Object composite);

    String nameOf(AssociationDescriptor descriptor);

    String nameOf(PropertyDescriptor property);

    String nameOf(ValueDescriptor property);

    String nameOf(@Optional PropertyDescriptor propDescriptor, @Optional AssociationDescriptor assocDescriptor);

    class Mixin
        implements PropertyCtrlFactory
    {
        @Structure
        ModuleSpi module;

        @Structure
        PolygeneSPI spi;

        @Structure
        ObjectFactory objectFactory;

        @Override
        public PropertyControl<?> createPropertyControl(PropertyDescriptor descriptor, boolean withLabel)
        {
            ValueType propertyType = descriptor.valueType();
            BiFunction<PropertyDescriptor, Boolean, PropertyControl<?>> factory = factories.get(propertyType);
            if (factory != null)
                return factory.apply(descriptor, withLabel);
            if (propertyType instanceof StatefulAssociationValueType<?>)
            {
                return objectFactory.newObject(EntityReferenceControl.class, descriptor, withLabel);
            } else if (propertyType instanceof CollectionType ctype)
            {
                ValueDescriptor valueDescriptor = module.typeLookup().lookupValueModel(ctype.collectedType().primaryType());
                if (valueDescriptor == null)
                {
                    if( ctype.isList() )
                    {
                        return objectFactory.newObject(ListPropertyControl.class, descriptor, withLabel);
                    }
                    if( ctype.isSet() )
                    {
                        return objectFactory.newObject(SetPropertyControl.class, descriptor, withLabel);
                    }
                }
                PropertyDescriptor propDescr = descriptor;
                return objectFactory.newObject(CompositeListPropertyControl.class, propDescr, valueDescriptor, withLabel);
            } else if (propertyType instanceof MapType)
            {
                return objectFactory.newObject(MapPropertyControl.class, descriptor, withLabel);
            } else if (propertyType instanceof EnumType)
            {
                return objectFactory.newObject(EnumPropertyControl.class, descriptor, withLabel);
            } else if (propertyType instanceof ArrayType)
            {
                return objectFactory.newObject(ArrayPropertyControl.class, descriptor, withLabel);
            }
            // We are here when a Property<X> is encountered, where X is a custom type.
            ValueDescriptor valueDescriptor = module.typeLookup().lookupValueModel(propertyType.primaryType());
            if (valueDescriptor != null)
            {
                return objectFactory.newObject(ValueLinkControl.class, descriptor);
            }
            return null;
        }

        @Override
        public AssociationControl<?> createAssociationControl(AssociationDescriptor descriptor, boolean withLabel)
        {
            return objectFactory.newObject(AssociationControl.class, descriptor, withLabel);
        }

        @Override
        public ManyAssociationControl<?> createManyAssociationControl(AssociationDescriptor descriptor, boolean withLabel)
        {
            return objectFactory.newObject(ManyAssociationControl.class, descriptor, withLabel);
        }

        @Override
        public NamedAssociationControl<?> createNamedAssociationControl(AssociationDescriptor descriptor, boolean withLabel)
        {
            return objectFactory.newObject(NamedAssociationControl.class, descriptor, withLabel);
        }

        @Override
        public void registerPropertyControlFactory(ValueType type, BiFunction<PropertyDescriptor, Boolean, PropertyControl<?>> factory)
        {
            factories.put(type, factory);
        }

        @Override
        public String nameOf(Object composite)
        {
            if (composite == null)
                return "<no object>";
            if (spi.isComposite(composite))
            {
                ModelDescriptor modelDescriptor = spi.modelDescriptorFor(composite);
                if (modelDescriptor instanceof StatefulAssociationCompositeDescriptor descriptor)
                {
                    List<? extends MetaInfoHolder> props = descriptor.state()
                        .properties()
                        .toList();
                    List<? extends MetaInfoHolder> assocs = descriptor.state()
                        .associations()
                        .toList();
                    List<MetaInfoHolder> list = new ArrayList<>(props);
                    list.addAll(assocs);
                    String result = list.stream()
                        .filter(p -> p.metaInfo(RenderAsName.class) != null)
                        .sorted(new MemberOrderComparator())
                        .map(m ->
                        {
                            AssociationStateHolder state;
                            if (composite instanceof EntityComposite c)
                                state = spi.stateOf(c);
                            else if (composite instanceof ValueComposite c)
                                state = spi.stateOf(c);
                            else
                                throw new IllegalArgumentException("Only Entities and Values can be used in JavaFX UI system");
                            Function<Object, String> formatter = instantiateFormatter(m.metaInfo(RenderAsName.class).format());
                            if (m instanceof PropertyDescriptor p)
                            {
                                Property<Object> property = state.propertyFor(p.accessor());
                                return formatter.apply(property);
                            }
                            if (m instanceof AssociationDescriptor a)
                            {
                                Association<?> assoc = state.associationFor(a.accessor());
                                return formatter.apply(assoc);
                            }
                            return null;
                        })
                        .filter(Objects::nonNull)
                        .collect(Collectors.joining(" - "));
                    return result;
                }
            }
            throw new IllegalArgumentException("Only Stateful Composites can be used as PropertyControl: " + composite);
        }

        private Function<Object, String> instantiateFormatter(Class<? extends Function<Object, String>> format)
        {
            try
            {
                return format.getConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e)
            {
                throw new RuntimeException(e);
            }
        }

        @Override
        public String nameOf(PropertyDescriptor propDescriptor, AssociationDescriptor assocDescriptor)
        {
            if (propDescriptor != null)
                return nameOf(propDescriptor);
            if (assocDescriptor != null)
                return nameOf(assocDescriptor);
            return null;
        }

        @Override
        public String nameOf(ValueDescriptor descriptor)
        {
            RenderAsValue renderAsValue = descriptor.metaInfo(RenderAsValue.class);
            if (renderAsValue != null)
                return renderAsValue.title();
            return humanize(descriptor.toString());
        }

        @Override
        public String nameOf(PropertyDescriptor property)
        {
            RenderAsValue renderAsValue = property.metaInfo(RenderAsValue.class);
            if (renderAsValue != null)
                return renderAsValue.title();
            return humanize(property.qualifiedName().name());
        }

        @Override
        public String nameOf(AssociationDescriptor assoc)
        {
            return humanize(assoc.qualifiedName().name());
        }

        static String humanize(String name)
        {
            int nameLength = name.length();
            StringBuilder humanName = new StringBuilder(nameLength);
            for (int i = 0; i < nameLength; i++)
            {
                char ch = name.charAt(i);
                if (Character.isUpperCase(ch))
                {
                    humanName.append(" ");
                }
                if (i == 0)
                {
                    ch = Character.toUpperCase(ch);
                }
                humanName.append(ch);
            }
            return humanName.toString();
        }

        private final Map<ValueType, BiFunction<PropertyDescriptor, Boolean, PropertyControl<?>>> factories = new HashMap<>();

        {
            factories.put(ValueType.STRING, (property, withLabel) -> objectFactory.newObject(StringPropertyControl.class, property, withLabel));
            factories.put(ValueType.IDENTITY, (property, withLabel) -> objectFactory.newObject(IdentityPropertyControl.class, property, withLabel));
            factories.put(ValueType.ENTITY_REFERENCE, (property, withLabel) -> objectFactory.newObject(EntityReferenceControl.class, property, withLabel));
            factories.put(ValueType.LONG, (property, withLabel) -> objectFactory.newObject(LongPropertyControl.class, property, withLabel));
            factories.put(ValueType.ZONED_DATE_TIME, (property, withLabel) -> objectFactory.newObject(ZonedDateTimePropertyControl.class, property, withLabel));
            factories.put(ValueType.OFFSET_DATE_TIME, (property, withLabel) -> objectFactory.newObject(OffsetDateTimePropertyControl.class, property, withLabel));
            factories.put(ValueType.LOCAL_DATE_TIME, (property, withLabel) -> objectFactory.newObject(LocalDateTimePropertyControl.class, property, withLabel));
            factories.put(ValueType.LOCAL_DATE, (property, withLabel) -> objectFactory.newObject(LocalDatePropertyControl.class, property, withLabel));
            factories.put(ValueType.LOCAL_TIME, (property, withLabel) -> objectFactory.newObject(LocalTimePropertyControl.class, property, withLabel));
            factories.put(ValueType.INSTANT, (property, withLabel) -> objectFactory.newObject(InstantPropertyControl.class, property, withLabel));
            factories.put(ValueType.DURATION, (property, withLabel) -> objectFactory.newObject(DurationPropertyControl.class, property, withLabel));
            factories.put(ValueType.PERIOD, (property, withLabel) -> objectFactory.newObject(PeriodPropertyControl.class, property, withLabel));
            factories.put(ValueType.CHARACTER, (property, withLabel) -> objectFactory.newObject(CharacterPropertyControl.class, property, withLabel));
            factories.put(ValueType.BOOLEAN, (property, withLabel) -> objectFactory.newObject(BooleanPropertyControl.class, property, withLabel));
            factories.put(ValueType.INTEGER, (property, withLabel) -> objectFactory.newObject(IntegerPropertyControl.class, property, withLabel));
            factories.put(ValueType.SHORT, (property, withLabel) -> objectFactory.newObject(ShortPropertyControl.class, property, withLabel));
            factories.put(ValueType.BYTE, (property, withLabel) -> objectFactory.newObject(BytePropertyControl.class, property, withLabel));
            factories.put(ValueType.FLOAT, (property, withLabel) -> objectFactory.newObject(FloatPropertyControl.class, property, withLabel));
            factories.put(ValueType.DOUBLE, (property, withLabel) -> objectFactory.newObject(DoublePropertyControl.class, property, withLabel));
            factories.put(ValueType.BIG_DECIMAL, (property, withLabel) -> objectFactory.newObject(BigDecimalPropertyControl.class, property, withLabel));
            factories.put(ValueType.BIG_INTEGER, (property, withLabel) -> objectFactory.newObject(BigIntegerPropertyControl.class, property, withLabel));
        }
    }
}
