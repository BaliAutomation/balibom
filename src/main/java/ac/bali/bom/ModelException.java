package ac.bali.bom;

public class ModelException extends Exception
{
    public ModelException(String message)
    {
        super(message);
    }

    public ModelException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
