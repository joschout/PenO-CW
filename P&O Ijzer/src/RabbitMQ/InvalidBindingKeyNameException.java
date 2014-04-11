package RabbitMQ;

public class InvalidBindingKeyNameException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	public InvalidBindingKeyNameException(){
		super();
	}
	
	public InvalidBindingKeyNameException(String exceptionMessage){
		super(exceptionMessage);
	}
}
