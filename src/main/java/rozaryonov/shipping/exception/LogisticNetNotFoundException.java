package rozaryonov.shipping.exception;

public class LogisticNetNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public LogisticNetNotFoundException () {} 
	public LogisticNetNotFoundException (String message) {
		super(message);
	}

}
