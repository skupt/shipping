package rozaryonov.shipping.exception;

public class PathCalculationException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public PathCalculationException() {}
	public PathCalculationException(String message) {
		super(message);
	}

}
