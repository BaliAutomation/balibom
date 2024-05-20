package org.qi4j.library.javafx.support;

import org.apache.polygene.api.common.Optional;
import org.apache.polygene.api.property.Property;

public interface FieldDescriptor
{
    Property<String> name();

    Property<Class<?>> type();

    Property<Integer> height();

    Property<Integer> width();
}
