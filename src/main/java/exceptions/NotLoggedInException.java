package exceptions;

public class NotLoggedInException extends RuntimeException {
	private static final long serialVersionUID = -5371382106535600601L;

	@Override
	public String toString() {
		return "NotLoggedInException []";
	}

	public NotLoggedInException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public NotLoggedInException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public NotLoggedInException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public NotLoggedInException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public NotLoggedInException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
	
	
}
