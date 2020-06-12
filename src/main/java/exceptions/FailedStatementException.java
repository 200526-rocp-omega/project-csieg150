package exceptions;

public class FailedStatementException extends RuntimeException {
	private static final long serialVersionUID = -3870717498918217445L;

	public FailedStatementException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public FailedStatementException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public FailedStatementException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public FailedStatementException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public FailedStatementException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "FailedStatementException []";
	}

}
