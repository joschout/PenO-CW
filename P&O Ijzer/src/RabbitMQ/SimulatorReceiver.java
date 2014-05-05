package RabbitMQ;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import simulator.Simulator;
import zeppelin.MainProgramImpl;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;

public class SimulatorReceiver implements Runnable {
	
	private SimulatorMessageParser parser;
	private Simulator simulator;
	private Channel channel;
	private String queueName;
	private static final String EXCHANGE_NAME = "server";	
	
	public SimulatorReceiver(Simulator simulator, Connection connection) {
		this.initialiseChannel(connection, simulator.getName());
	}
	
	public void run() {
		QueueingConsumer consumer = null;
		try {
			consumer = new QueueingConsumer(channel);
			this.getChannel().basicConsume(this.queueName, true, consumer);
			parser = new SimulatorMessageParser(this.simulator);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		while (true) {
			try {
				QueueingConsumer.Delivery delivery = consumer.nextDelivery();
				String message = new String(delivery.getBody());
				String routingKey = delivery.getEnvelope().getRoutingKey();
				parser.parse(routingKey, message);
			} catch (ShutdownSignalException e) {
				e.printStackTrace();
			} catch (ConsumerCancelledException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (InvalidBindingKeyException e) {
				e.printStackTrace();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static List<String> generateBindingKeys(String simulatorName) {
		List<String> toReturn = new ArrayList<String>();
		toReturn.add("*.info");
		toReturn.add(simulatorName + ".hcommand.*");
		toReturn.add(simulatorName + ".lcommand.*");
		toReturn.add(simulatorName + ".private.#");
		return toReturn;
	}
	
	public Channel getChannel() {
		return this.channel;
	}
	
	public void setChannel(Channel channel) {
		this.channel = channel;
	}
	
	public String getQueueName() {
		return this.queueName;
	}
	
	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}
	
	private void initialiseChannel(Connection connection, String simulatorName) {
		try {
			this.setChannel(connection.createChannel());
			this.getChannel().exchangeDeclare(EXCHANGE_NAME, "topic");
			this.setQueueName(this.getChannel().queueDeclare().getQueue());

			for(String bindingKey : generateBindingKeys(simulatorName)){    
				this.getChannel().queueBind(queueName, EXCHANGE_NAME, bindingKey);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
