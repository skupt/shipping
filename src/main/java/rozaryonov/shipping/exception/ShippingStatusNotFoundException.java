package rozaryonov.shipping.exception;

public class ShippingStatusNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	public ShippingStatusNotFoundException() {}
	public ShippingStatusNotFoundException(String message) {
		super(message);
	}

}
