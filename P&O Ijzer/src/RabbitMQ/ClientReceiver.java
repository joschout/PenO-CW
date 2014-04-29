package RabbitMQ;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;

import coordinate.SwingApp;

public class ClientReceiver implements Runnable {

	private ClientMessageParser parser;
	private Channel channel;
	private String queueName;
	private static final String EXCHANGE_NAME = "server";	
	private SwingApp app;

	public ClientReceiver(SwingApp app, Connection connection) {
		this.initialiseChannel(connection);
		this.app = app;
	}

	public static String[] generateBindingKeys(){
		List<String> bindingKeysList = new ArrayList<String>();

		// <teamkleur kleine letters>.info.location <x>,<y> (int) (millimeter) 
		// <teamkleur kleine letters>.info.height <z> (int) (millimeter) 
		bindingKeysList.add("*.info.*");

		//<teamkleur kleine letters>.hcommand.move <x>,<y> (int) (millimeter) 
		//<teamkleur kleine letters>.hcommand.elevate <z> (int) (millimeter) 
		bindingKeysList.add("ijzer.hcommand.*");

		//<teamkleur kleine letters>.lcommand.motor1 [-100,100] (int) (percent) 
		//<teamkleur kleine letters>.lcommand.motor2 [-100,100] (int) (percent) 
		//<teamkleur kleine letters>.lcommand.motor3 [-100,100] (int) (percent) 
		bindingKeysList.add("ijzer.lcommand.*");

		//<teamkleur kleine letters>.private.# vrij te kiezen 
		bindingKeysList.add("ijzer.private.#");
		
		bindingKeysList.add("ijzer.tablets.*");
		
		
		String[] bindingKeysArray = bindingKeysList.toArray(new String[bindingKeysList.size()]);
		return bindingKeysArray;
	}


	public void run() {
		QueueingConsumer consumer = null;
		try {
			consumer = new QueueingConsumer(channel);
			this.getChannel().basicConsume(this.queueName, true, consumer);
			parser = new ClientMessageParser(getApp());
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

	public SwingApp getApp() {
		return app;
	}

	public void setApp(SwingApp app) {
		this.app = app;
	}

	private void initialiseChannel(Connection connection) {
		try {
			this.setChannel(connection.createChannel());
			this.getChannel().exchangeDeclare(EXCHANGE_NAME, "topic");
			this.setQueueName(this.getChannel().queueDeclare().getQueue());

			for(String bindingKey : generateBindingKeys()){    
				this.getChannel().queueBind(queueName, EXCHANGE_NAME, bindingKey);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
