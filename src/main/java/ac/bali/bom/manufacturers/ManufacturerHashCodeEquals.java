package ac.bali.bom.manufacturers;

import org.apache.polygene.api.injection.scope.This;

public abstract class ManufacturerHashCodeEquals
    implements Manufacturer
{
    @This
    Manufacturer me;

    @Override
    public int hashCode()
    {
        return me.identifier().get().trim().toLowerCase().substring(0, 5).hashCode();
    }

    @SuppressWarnings("RedundantIfStatement")
    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
            return false;
        if (obj instanceof Manufacturer other)
        {

            if (other.identifier().get().equals(me.identifier().get()))
            {
                return true;
            }
            if (other.alternateNames().get().contains(me.identifier().get()))
            {
                return true;
            }
            if (me.alternateNames().get().contains(other.identifier().get()))
            {
                return true;
            }
        }
        return false;
    }
}
