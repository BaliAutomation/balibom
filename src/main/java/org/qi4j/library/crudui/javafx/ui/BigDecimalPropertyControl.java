package org.qi4j.library.crudui.javafx.ui;

import java.math.BigDecimal;
import org.apache.polygene.api.injection.scope.Service;
import org.apache.polygene.api.injection.scope.Uses;
import org.apache.polygene.api.property.PropertyDescriptor;

public class BigDecimalPropertyControl extends NumericPropertyControl<BigDecimal>
{
    public BigDecimalPropertyControl(@Service PropertyCtrlFactory factory, @Uses PropertyDescriptor descriptor, @Uses boolean withLabel)
    {
        super(factory, descriptor, withLabel);
    }
}
