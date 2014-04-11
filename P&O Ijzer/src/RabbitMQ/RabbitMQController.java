package RabbitMQ;

public class RabbitMQController {

	private ZeppelinReciever recv;
	
	public ZeppelinReciever getRecv() {
		return recv;
	}

	public void setRecv(ZeppelinReciever recv) {
		this.recv = recv;
	}

	public RabbitMQController(){
	 setRecv(new ZeppelinReciever(ZeppelinReciever.generateBindingKeys()));
		
		
	}
	
	
}
