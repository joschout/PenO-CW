package RabbitMQ;

import zeppelin.MainProgramImpl;

public class RabbitMQControllerClient extends RabbitMQController {

	public RabbitMQControllerClient(MainProgramImpl zeppelin) {
		super(zeppelin);
		Thread clientThread = new Thread(new ZeppelinReceiver(zeppelin, this.getConnection()));
		clientThread.start();
	}

	private ClientReceiver cRecv;
	
	public ClientReceiver getCRecv() {
		return cRecv;
	}

	public void setCRecv(ClientReceiver cRecv) {
		this.cRecv = cRecv;
	}
}
