package rozaryonov.shipping.exception;

public class GuestSerivceException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public GuestSerivceException () {}
	public GuestSerivceException (String message) {
		super(message);
	}

}
