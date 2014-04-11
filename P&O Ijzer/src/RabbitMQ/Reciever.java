package RabbitMQ;

import java.util.ArrayList;
import java.util.List;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.QueueingConsumer;

public class Reciever {

	private static final String EXCHANGE_NAME = "server";	
	
	public static String[] generateBindingKeys(){
	/*	<teamkleur kleine letters>.info.location <x>,<y> (int) (millimeter) 
		<teamkleur kleine letters>.info.height <z> (int) (millimeter) 
		<teamkleur kleine letters>.hcommand.move <x>,<y> (int) (millimeter) 
		<teamkleur kleine letters>.hcommand.elevate <z> (int) (millimeter) 
		<teamkleur kleine letters>.lcommand.motor1 [-100,100] (int) (percent) 
		<teamkleur kleine letters>.lcommand.motor2 [-100,100] (int) (percent) 
		<teamkleur kleine letters>.lcommand.motor3 [-100,100] (int) (percent) 
		<teamkleur kleine letters>.private.# vrij te kiezen 
	*/
		List<String> bindingKeysList = new ArrayList<String>();
		
		// <teamkleur kleine letters>.info.location <x>,<y> (int) (millimeter) 
		// <teamkleur kleine letters>.info.height <z> (int) (millimeter) 
		bindingKeysList.add("*.info.*");
		
		//<teamkleur kleine letters>.hcommand.move <x>,<y> (int) (millimeter) 
		//<teamkleur kleine letters>.hcommand.elevate <z> (int) (millimeter) 
		bindingKeysList.add("*.hcommand.*");
		
		//<teamkleur kleine letters>.lcommand.motor1 [-100,100] (int) (percent) 
		//<teamkleur kleine letters>.lcommand.motor2 [-100,100] (int) (percent) 
		//<teamkleur kleine letters>.lcommand.motor3 [-100,100] (int) (percent) 
		bindingKeysList.add("*.lcommand.*");
		
		//<teamkleur kleine letters>.private.# vrij te kiezen 
		bindingKeysList.add("*.private.#");
		String[] bindingKeysArray = bindingKeysList.toArray(new String[bindingKeysList.size()]);
		return bindingKeysArray;
	}
	
	
	public Reciever(){
		Connection connection = null;
	    Channel channel = null;
	    try {
	      ConnectionFactory factory = new ConnectionFactory();
	      factory.setHost("localhost");
	  
	      connection = factory.newConnection();
	      channel = connection.createChannel();

	      channel.exchangeDeclare(EXCHANGE_NAME, "topic");
	      String queueName = channel.queueDeclare().getQueue();
	
	      for(String bindingKey : generateBindingKeys()){    
		        channel.queueBind(queueName, EXCHANGE_NAME, bindingKey);
		      }
		
	      QueueingConsumer consumer = new QueueingConsumer(channel);
	      channel.basicConsume(queueName, true, consumer);

	      while (true) {
	        QueueingConsumer.Delivery delivery = consumer.nextDelivery();
	        String message = new String(delivery.getBody());
	        String routingKey = delivery.getEnvelope().getRoutingKey();
	        
	        
	        
	       // System.out.println(" [x] Received '" + routingKey + "':'" + message + "'");   
	      }
	    }
	    catch  (Exception e) {
	      e.printStackTrace();
	    }
	    finally {
	      if (connection != null) {
	        try {
	          connection.close();
	        }
	        catch (Exception ignore) {}
	      }
	    }
	  }
		
	}
	