package RabbitMQ;

public class InvalidBindingKeyException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	public InvalidBindingKeyException(){
		super();
	}
	
	public InvalidBindingKeyException(String exceptionMessage){
		super(exceptionMessage);
	}
}
