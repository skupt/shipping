package rozaryonov.shipping.exception;

public class InvoiceStatusNotFound extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public InvoiceStatusNotFound () {} 
	public InvoiceStatusNotFound (String message) {
		super(message);
	}

}
