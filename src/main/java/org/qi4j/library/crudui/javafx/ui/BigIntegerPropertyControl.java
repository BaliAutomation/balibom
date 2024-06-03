package org.qi4j.library.crudui.javafx.ui;

import java.math.BigInteger;
import org.apache.polygene.api.injection.scope.Service;
import org.apache.polygene.api.injection.scope.Uses;
import org.apache.polygene.api.property.PropertyDescriptor;

public class BigIntegerPropertyControl extends NumericPropertyControl<BigInteger>
{
    public BigIntegerPropertyControl(@Service PropertyCtrlFactory factory, @Uses PropertyDescriptor descriptor, @Uses boolean withLabel)
    {
        super(factory, descriptor, withLabel);
    }
}
