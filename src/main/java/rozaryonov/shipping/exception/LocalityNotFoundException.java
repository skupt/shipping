package rozaryonov.shipping.exception;

public class LocalityNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public LocalityNotFoundException () {} 
	public LocalityNotFoundException (String message) {
		super(message);
	}

}
