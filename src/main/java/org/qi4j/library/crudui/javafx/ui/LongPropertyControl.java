package org.qi4j.library.crudui.javafx.ui;

import org.apache.polygene.api.injection.scope.Service;
import org.apache.polygene.api.injection.scope.Uses;
import org.apache.polygene.api.property.PropertyDescriptor;

public class LongPropertyControl extends NumericPropertyControl<Long>
{
    public LongPropertyControl(@Service PropertyCtrlFactory factory, @Uses PropertyDescriptor descriptor, @Uses boolean withLabel)
    {
        super(factory, descriptor, withLabel);
    }
}
