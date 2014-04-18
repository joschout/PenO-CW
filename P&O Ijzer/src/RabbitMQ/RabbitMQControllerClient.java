package RabbitMQ;

import zeppelin.MainProgramImpl;

public class RabbitMQControllerClient extends RabbitMQController {

	//private ClientReceiver cRecv;
	private ClientSender cSend;

	public RabbitMQControllerClient(MainProgramImpl zeppelin) {
		super(zeppelin);
		Thread clientThread = new Thread(new ClientReceiver(zeppelin, this.getConnection()));
		clientThread.start();
		setClientSender(new ClientSender(zeppelin, this.getConnection()));
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
