package rozaryonov.shipping.exception;

public class ManagerSerivceException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public ManagerSerivceException () {}
	public ManagerSerivceException (String message) {
		super(message);
	}

}
