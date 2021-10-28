package rozaryonov.shipping.exception;

public class ConnectionGettingException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public ConnectionGettingException (String message) {
		super(message);
	}
	public ConnectionGettingException (String message, Exception couse) {
		super(message);
	}

}
