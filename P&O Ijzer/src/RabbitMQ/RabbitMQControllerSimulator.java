package RabbitMQ;

import simulator.Simulator;

public class RabbitMQControllerSimulator extends RabbitMQController {
	
	private final SimulatorReceiver rec;
	private final SimulatorSender send;
	
	public RabbitMQControllerSimulator(Simulator simulator) {
		super();
		this.rec = new SimulatorReceiver(simulator, super.getConnection());
		Thread receiverThread = new Thread(rec);
		receiverThread.start();
		this.send = new SimulatorSender(simulator, super.getConnection());
	}
	
	public SimulatorReceiver getReceiver() {
		return this.rec;
	}
	
	public SimulatorSender getSender() {
		return this.send;
	}

}
