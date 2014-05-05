package RabbitMQ;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;


/**
 * Controllerklasse voor alles wat met RAbbitMQ te maken heeft.
 * @author Jonas
 *
 */
public abstract class RabbitMQController {
	
	private Connection connection;

	/**
	 * Constructor voor de controller
	 * @param zeppelin
	 */
	public RabbitMQController()
	{
		initialiseConnection();		
	}
	
	public Connection getConnection() {
		return this.connection;
	}
	
	public void closeConnection() {
		if (connection != null) {
	        try {
	          connection.close();
	        }
	        catch (Exception ignore) {}
	      }
	}
	
	/**
	 * Initialiseert de connection
	 */
	private void initialiseConnection() {
	    try {
	      ConnectionFactory factory = new ConnectionFactory();

	      factory.setHost("192.168.2.134");
	      factory.setUsername("ijzer");
	      factory.setPassword("ijzer");
	      factory.setPort(5672);
	  
	      connection = factory.newConnection();
	    }
	    catch  (Exception e) {
	      e.printStackTrace();
	    }
	}


	
	
}
