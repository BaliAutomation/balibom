package org.qi4j.library.javafx.support;

import org.qi4j.library.javafx.support.RenderAsName;
import java.util.Comparator;
import java.util.Optional;
import org.apache.polygene.api.composite.CompositeDescriptor;
import org.apache.polygene.api.composite.StatefulCompositeDescriptor;
import org.apache.polygene.api.entity.EntityComposite;
import org.apache.polygene.api.entity.EntityDescriptor;
import org.apache.polygene.api.injection.scope.Structure;
import org.apache.polygene.api.property.Property;
import org.apache.polygene.api.value.ValueComposite;
import org.apache.polygene.api.value.ValueDescriptor;
import org.apache.polygene.spi.PolygeneSPI;
import org.qi4j.library.javafx.ui.CompositePane;

public class EntityNameComparator
    implements Comparator<Object>
{
    @Structure
    PolygeneSPI spi;

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public int compare(Object o1, Object o2)
    {
        if (!spi.isComposite(o1) || !spi.isComposite(o2))
        {
            if (o1 instanceof Comparable<?>)
            {
                return ((Comparable<Object>) o1).compareTo(o2);
            }
            return 0;
        }
        StatefulCompositeDescriptor descriptor;
        if (ValueComposite.class.isAssignableFrom(o1.getClass()))
        {
            descriptor = spi.valueDescriptorFor(o1);
        } else if (EntityComposite.class.isAssignableFrom(o1.getClass()))
        {
            descriptor = spi.entityDescriptorFor(o1);
        } else
            return 0;
        Optional<Integer> result = descriptor.state()
            .properties()
            .filter(p -> p.metaInfo(RenderAsName.class) != null)
            .sorted(new MemberOrderComparator())
            .map(p ->
            {
                Property<Object> p1;
                Property<Object> p2;
                CompositeDescriptor compositeDescriptor = spi.compositeDescriptorFor(o1);
                if (compositeDescriptor instanceof ValueDescriptor)
                {
                    p1 = spi.stateOf(((ValueComposite) o1)).propertyFor(p.accessor());
                    p2 = spi.stateOf(((ValueComposite) o2)).propertyFor(p.accessor());
                } else if (compositeDescriptor instanceof EntityDescriptor)
                {
                    p1 = spi.stateOf(((EntityComposite) o1)).propertyFor(p.accessor());
                    p2 = spi.stateOf(((EntityComposite) o2)).propertyFor(p.accessor());
                } else
                    return 0;
                Object c1 = p1.get();
                Object c2 = p2.get();
                if (c1 instanceof Comparable && c2 instanceof Comparable)
                {
                    return ((Comparable) c1).compareTo(c2);
                }
                return 0;
            }).filter(r -> r != 0).findFirst();
        return result.orElse(0);
    }
}
