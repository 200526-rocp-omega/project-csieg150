package exceptions;

public class AuthorizationException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6839932724517860758L;

	@Override
	public String toString() {
		return "AuthorizationException []";
	}

	public AuthorizationException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AuthorizationException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public AuthorizationException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public AuthorizationException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public AuthorizationException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
