package exceptions;

public class IllegalBalanceException extends RuntimeException { 
	// Exception is used to handle when Users try and input invalid amounts to withdraw/deposit
	private static final long serialVersionUID = 2540130459953428511L;

	@Override
	public String toString() {
		return "IllegalBalanceException []";
	}

	public IllegalBalanceException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public IllegalBalanceException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public IllegalBalanceException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public IllegalBalanceException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public IllegalBalanceException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
