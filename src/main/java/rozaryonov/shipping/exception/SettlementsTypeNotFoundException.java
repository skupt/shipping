package rozaryonov.shipping.exception;

public class SettlementsTypeNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public SettlementsTypeNotFoundException () {} 
	public SettlementsTypeNotFoundException (String message) {
		super(message);
	}

}
