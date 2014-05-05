package RabbitMQ;

import java.io.IOException;

import simulator.Simulator;
import logger.LogWriter;
import zeppelin.MainProgramImpl;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public class SimulatorSender {

	private Simulator simulator;
	private Channel channel;
	private static final String EXCHANGE_NAME = "server";

	public SimulatorSender(Simulator simulator, Connection connection) {
		this.initialiseChannel(connection);
		this.simulator = simulator;
	}

	public Simulator getSimulator() {
		return this.simulator;
	}

	public Channel getChannel() {
		return this.channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public void sendLocation(){
		double xInCM = this.getSimulator().getPosition().x;
		double yInCM = this.getSimulator().getPosition().y;

		int xInMM = (int)xInCM*100;
		int yInMM = (int)yInCM*100;

		String xAsString = String.valueOf(xInMM);
		String yAsString = String.valueOf(yInMM);

		String routingKey = this.getSimulator().getName().toLowerCase() + ".info.location";
		String message = xAsString + "," +yAsString;

		try {
			channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendHeight(){
		double heightInCm = this.getSimulator().getHeight();
		int heightInMM = (int)heightInCm*100;

		String routingKey = this.getSimulator().getName().toLowerCase() + ".info.height";
		String message = String.valueOf(heightInMM);
		try {
			channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendPrivateMessage(PrivateRoutingKeyTypes type){
		
	}


	private void initialiseChannel(Connection connection) {
		try {
			this.setChannel(connection.createChannel());
			this.getChannel().exchangeDeclare(EXCHANGE_NAME, "topic");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
