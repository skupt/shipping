package rozaryonov.shipping.exception;

public class PaymentCreationException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public PaymentCreationException() {}
	public PaymentCreationException(String message) {
		super(message);
	}
}
