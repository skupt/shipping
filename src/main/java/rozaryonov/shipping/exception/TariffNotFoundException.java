package rozaryonov.shipping.exception;

public class TariffNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public TariffNotFoundException () {} 
	public TariffNotFoundException (String message) {
		super(message);
	}

}
