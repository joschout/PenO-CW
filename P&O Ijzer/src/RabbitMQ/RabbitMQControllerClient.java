package RabbitMQ;

import coordinate.SwingApp;
import zeppelin.MainProgramImpl;

public class RabbitMQControllerClient extends RabbitMQController {

	//private ClientReceiver cRecv;
	private ClientSender cSend;

	public RabbitMQControllerClient(SwingApp app) {
		super();
		Thread clientThread = new Thread(new ClientReceiver(app, this.getConnection()));
		clientThread.start();
		setClientSender(new ClientSender( this.getConnection()));
	}

//	public ClientReceiver getCRecv() {
//		return cRecv;
//	}
//
//	public void setCRecv(ClientReceiver cRecv) {
//		this.cRecv = cRecv;
//	}

	public ClientSender getClientSender() {
		return cSend;
	}

	public void setClientSender(ClientSender cSend) {
		this.cSend = cSend;
	}
	
	
	
	
	
	
}
