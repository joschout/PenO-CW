package RabbitMQ;

import zeppelin.MainProgramImpl;

public class RabbitMQControllerZeppelin extends RabbitMQController {

	private ZeppelinReceiver zRecv;


	public RabbitMQControllerZeppelin(MainProgramImpl zeppelin) {
		super(zeppelin);
		Thread receiverThread = new Thread(new ZeppelinReceiver(zeppelin, this.getConnection()));
		receiverThread.start();
	}
	
	public ZeppelinReceiver getZRecv() {
		return zRecv;
	}

	public void setZRecv(ZeppelinReceiver recv) {
		this.zRecv = recv;
	}
}
