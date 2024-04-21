package org.qi4j.library.javafx.support;

import java.lang.reflect.Type;
import java.util.Comparator;
import org.apache.polygene.api.association.AssociationDescriptor;
import org.apache.polygene.api.common.QualifiedName;
import org.apache.polygene.api.property.PropertyDescriptor;
import org.apache.polygene.api.structure.MetaInfoHolder;

public class MemberOrderComparator
    implements Comparator<MetaInfoHolder>
{
    public static final MemberOrderComparator INSTANCE = new MemberOrderComparator();

    @Override
    public int compare(MetaInfoHolder o1, MetaInfoHolder o2)
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
        Type t1;
        Type t2;
        QualifiedName n1;
        QualifiedName n2;
        if (o1 instanceof PropertyDescriptor)
        {
            t1 = ((PropertyDescriptor) o1).type();
            n1 = ((PropertyDescriptor) o1).qualifiedName();
        } else
        {
            t1 = ((AssociationDescriptor) o1).type();
            n1 = ((AssociationDescriptor) o1).qualifiedName();
        }
        if (o2 instanceof PropertyDescriptor)
        {
            t2 = ((PropertyDescriptor) o2).type();
            n2 = ((PropertyDescriptor) o2).qualifiedName();
        } else
        {
            t2 = ((AssociationDescriptor) o2).type();
            n2 = ((AssociationDescriptor) o2).qualifiedName();
        }
        if (t1.equals(t2))
            return n1.name().compareTo(n2.name());
        if (t1.equals(String.class))
            return 1;
        return -1;
    }
}

