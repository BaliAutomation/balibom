package org.qi4j.library.crudui;

import java.util.Objects;
import java.util.function.Function;

public class ToStringFormatter
    implements Function<Object,String>
{
    @Override
    public String apply(Object s)
    {
        return Objects.toString(s);
    }
}
