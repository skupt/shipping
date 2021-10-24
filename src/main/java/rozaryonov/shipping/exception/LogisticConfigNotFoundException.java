package rozaryonov.shipping.exception;

public class LogisticConfigNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public LogisticConfigNotFoundException () {} 
	public LogisticConfigNotFoundException (String message) {
		super(message);
	}

}
