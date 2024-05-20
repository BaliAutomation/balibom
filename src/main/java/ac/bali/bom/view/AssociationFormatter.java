package ac.bali.bom.view;

import java.util.function.Function;
import org.apache.polygene.api.association.Association;

public class AssociationFormatter
    implements Function<Object,String>
{
    @Override
    public String apply(Object obj)
    {
        if( obj instanceof Association<?> association)
        {
            String identity = association.reference().identity().toString();
            int index = identity.indexOf('_');
            if (index < 0)
                return identity;
            return identity.substring(index + 1);
        }
        else
        {
            return obj.toString();
        }
    }
}
