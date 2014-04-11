package RabbitMQ;

public class RabbitMQController {

	private ZeppelinReceiver recv;
	
	public ZeppelinReceiver getRecv() {
		return recv;
	}

	public void setRecv(ZeppelinReceiver recv) {
		this.recv = recv;
	}

	public RabbitMQController(){
	 setRecv(new ZeppelinReceiver(ZeppelinReceiver.generateBindingKeys()));
		
		
	}
	
	
}
