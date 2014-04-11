package RabbitMQ;

public class RabbitMQController {

	private Reciever recv;
	
	public Reciever getRecv() {
		return recv;
	}

	public void setRecv(Reciever recv) {
		this.recv = recv;
	}

	public RabbitMQController(){
	 setRecv(new Reciever(Reciever.generateBindingKeys()));
		
		
	}
	
	
}
