package ac.bali.bom.ui;

import ac.bali.bom.support.PropertyOrderComparator;
import ac.bali.bom.support.RenderAsName;
import org.apache.polygene.api.association.AssociationDescriptor;
import org.apache.polygene.api.association.AssociationStateHolder;
import org.apache.polygene.api.composite.StatefulCompositeDescriptor;
import org.apache.polygene.api.composite.TransientComposite;
import org.apache.polygene.api.entity.EntityComposite;
import org.apache.polygene.api.injection.scope.Structure;
import org.apache.polygene.api.mixin.Mixins;
import org.apache.polygene.api.object.ObjectFactory;
import org.apache.polygene.api.property.Property;
import org.apache.polygene.api.property.PropertyDescriptor;
import org.apache.polygene.api.type.*;
import org.apache.polygene.api.value.ValueComposite;
import org.apache.polygene.spi.PolygeneSPI;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

@Mixins(PropertyCtrlFactory.Mixin.class)
public interface PropertyCtrlFactory
{
    <T> CompositePane<T> createPane(T composite);

    PropertyControl createPropertyControl(PropertyDescriptor descriptor, boolean withLabel);

    void registerPropertyControlFactory(ValueType type, BiFunction<PropertyDescriptor, Boolean, PropertyControl> factory);

    String nameOf(Object composite);

    String nameOf(AssociationDescriptor descriptor);

    String nameOf(PropertyDescriptor property);

    class Mixin
        implements PropertyCtrlFactory
    {
        @Structure
        PolygeneSPI spi;

        @Structure
        ObjectFactory objectFactory;

        @Override
        public <T> CompositePane<T> createPane(T composite)
        {
            return null;
        }

        @Override
        public PropertyControl createPropertyControl(PropertyDescriptor descriptor, boolean withLabel)
        {
            ValueType propertyType = descriptor.valueType();
            BiFunction<PropertyDescriptor, Boolean, PropertyControl> factory = factories.get(propertyType);
            if (factory != null)
                return factory.apply(descriptor, withLabel);
//        if (propertyType instanceof ValueCompositeType)
//        {
//        } else if (propertyType instanceof EntityCompositeType)
//        {
//        } else
            if (propertyType instanceof StatefulAssociationValueType<?>)
            {
                return objectFactory.newObject(EntityReferencePropertyControl.class, descriptor);
            } else if (propertyType instanceof CollectionType)
            {
                return objectFactory.newObject(ListPropertyControl.class, descriptor);
            } else if (propertyType instanceof MapType)
            {
                return objectFactory.newObject(MapPropertyControl.class, descriptor);
            } else if (propertyType instanceof EnumType)
            {
                return objectFactory.newObject(EnumPropertyControl.class, descriptor);
            } else if (propertyType instanceof ArrayType)
            {
                return objectFactory.newObject(ArrayPropertyControl.class, descriptor);
            }
            return null;
        }

        @Override
        public void registerPropertyControlFactory(ValueType type, BiFunction<PropertyDescriptor, Boolean, PropertyControl> factory)
        {
            factories.put(type, factory);
        }

        @Override
        public String nameOf(Object composite)
        {
            if (composite == null)
                return "<no object>";
            StatefulCompositeDescriptor descriptor;
            if (ValueComposite.class.isAssignableFrom(composite.getClass()))
            {
                descriptor = spi.valueDescriptorFor(composite);
            } else if (EntityComposite.class.isAssignableFrom(composite.getClass()))
            {
                descriptor = spi.entityDescriptorFor(composite);
            } else if (TransientComposite.class.isAssignableFrom(composite.getClass()))
            {
                descriptor = spi.transientDescriptorFor(composite);
            } else
            {
                throw new IllegalArgumentException("Only Stateful Composites can be used as PropertyControl");
            }

            return descriptor.state()
                .properties()
                .sorted(new PropertyOrderComparator())
                .filter(p -> p.metaInfo(RenderAsName.class) != null)
                .map(p ->
                {
                    AssociationStateHolder state = spi.stateOf((ValueComposite) composite);
                    Property<?> property = state.propertyFor(p.accessor());
                    return property.get().toString();
                }).collect(Collectors.joining(" - "));
        }

        @Override
        public String nameOf(PropertyDescriptor property)
        {
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

        private final Map<ValueType, BiFunction<PropertyDescriptor, Boolean, PropertyControl>> factories = new HashMap<>();

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
