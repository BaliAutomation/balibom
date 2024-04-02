package ac.bali.bom.support;

import org.apache.polygene.api.property.PropertyDescriptor;

import java.lang.reflect.Type;
import java.util.Comparator;

public class PropertyOrderComparator
    implements Comparator<PropertyDescriptor>
{
    @Override
    public int compare(PropertyDescriptor o1, PropertyDescriptor o2)
    {
        int sort1 = -1;
        int sort2 = -1;
        Order order1 = o1.metaInfo(Order.class);
        if (order1 != null)
            sort1 = order1.value();
        Order order2 = o2.metaInfo(Order.class);
        if (order2 != null)
            sort2 = order2.value();
        if (sort1 > 0 && sort2 > 0)
            return sort1 - sort2;
        if (sort1 > 0)
            return -1;
        if (sort2 > 0)
            return 1;
        Type t1 = o1.type();
        Type t2 = o2.type();
        if (t1.equals(t2))
            return o1.qualifiedName().name().compareTo(o2.qualifiedName().name());
        if (t1.equals(String.class))
            return 1;
        return -1;
    }
}
