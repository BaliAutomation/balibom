package org.qi4j.library.crudui;

import org.apache.polygene.api.property.Property;
import org.apache.polygene.api.type.ValueType;

public interface FieldDescriptor
{
    Property<String> name();

    Property<ValueType> type();

    Property<Integer> height();

    Property<Integer> width();
}
