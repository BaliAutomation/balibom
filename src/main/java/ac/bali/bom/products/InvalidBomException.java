package ac.bali.bom.products;

import ac.bali.bom.ModelException;

public class InvalidBomException extends ModelException
{
    public InvalidBomException(String message)
    {
        super(message);
    }
}
