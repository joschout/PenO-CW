package RabbitMQ;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import zeppelin.MainProgramImpl;

public class RabbitMQController {

	private Connection connection;
	private ZeppelinReceiver recv;
	
	
	public RabbitMQController(MainProgramImpl zeppelin)
	{
		initialiseConnection();
		Thread receiverThread = new Thread(new ZeppelinReceiver(zeppelin, this.getConnection()));
		receiverThread.start();
	}
	
	public ZeppelinReceiver getRecv() {
		return recv;
	}

	public void setRecv(ZeppelinReceiver recv) {
		this.recv = recv;
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
	
	private void initialiseConnection() {
	    try {
	      ConnectionFactory factory = new ConnectionFactory();
	      factory.setHost("localhost");
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
