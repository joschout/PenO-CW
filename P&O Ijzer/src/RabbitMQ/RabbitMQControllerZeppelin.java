package RabbitMQ;

import zeppelin.MainProgramImpl;

public class RabbitMQControllerZeppelin extends RabbitMQController {

	private ZeppelinReceiver zRecv;
	private ZeppelinSender zSend;
	

	public RabbitMQControllerZeppelin(MainProgramImpl zeppelin) {
		super();
		this.zRecv = new ZeppelinReceiver(zeppelin, super.getConnection());
		Thread receiverThread = new Thread(this.zRecv);
		receiverThread.start();
		this.zSend = new ZeppelinSender(zeppelin, this.getConnection());
	}
	
	public ZeppelinReceiver getZeppelinReceiver() {
		return zRecv;
	}

	public void setZeppelinReceiver(ZeppelinReceiver recv) {
		this.zRecv = recv;
	}

	public ZeppelinSender getZeppelinSender() {
		return zSend;
	}

	public void setZeppelinSender(ZeppelinSender zSend) {
		this.zSend = zSend;
	}
}
