package ac.bali.bom.suppliers.oauth2;

public class AuthorizationException extends Exception {
	public AuthorizationException (String message)
	{
		super(message);
	}

	public AuthorizationException(String message, Throwable cause)
	{
		super(message, cause);
	}
}