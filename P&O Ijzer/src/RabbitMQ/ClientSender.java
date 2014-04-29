package RabbitMQ;

import java.io.IOException;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import coordinate.GridPoint;

public class ClientSender {
	
	private Channel channel;
	private static final String EXCHANGE_NAME = "server";

	public ClientSender(Connection connection) {
		this.initialiseChannel(connection);
	}
	

	public Channel getChannel() {
		return this.channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}
	
	
	public void sendHCommand_ElevateIjzer(double height){
		sendHCommand_Elevate(height, "ijzer");
	}
	
	public void sendHCommand_Elevate(double height, String groupName){
		double heightInMM = height * 100;
		String message = String.valueOf((int)heightInMM);
		String routingKey = groupName + ".hcommand.elevate";
		try {
			channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendHCommand_MoveIjzer(GridPoint position){
		sendHCommand_Move(position, "ijzer");
	}
	
	public void sendHCommand_Move(GridPoint position, String groupName){
		double xInMM = position.x*100;
		double yInMM = position.y*100;
		
		String routingKey = groupName + ".hcommand.move";
		String message = String.valueOf((int)xInMM) + "," + String.valueOf((int)yInMM);
		try {
			channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	public void sendPrivateMessage_exit(){
		String routingKey ="ijzer.private" + PrivateRoutingKeyTypes.EXIT.getRoutingKeyPart();
		String message="";
		try {
			channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public void sendPrivateMessage_getTargetHeight(){
		String routingKey = "ijzer.private" + PrivateRoutingKeyTypes.GETTARGETHEIGHT;
		String message="";
		try {
			channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendPrivateMessage_setTargetHeight(double targetHeight){
		String routingKey = "ijzer.private" + PrivateRoutingKeyTypes.SETTARGETHEIGHT;
		String message=String.valueOf(targetHeight);;
		try {
			channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public void sendPrivateMessage_PID_getKValue(PrivateRoutingKeyTypes type){
		if(type.getRoutingKeyPart().matches("\\.height\\.get[PID]")||type.getRoutingKeyPart().matches("\\.angle\\.get[PID]")){
			String lastRoutingKeyPart = type.getRoutingKeyPart();
			String firstRoutingKeyPart = "ijzer.private";
			String routingKey = firstRoutingKeyPart + lastRoutingKeyPart;
		

			String message="";
			try {
				channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else throw new InvalidBindingKeyException("Het meegegeven type is verkeerd");
	}
	
	
	public void sendPrivateMessag_PID_setKValue(PrivateRoutingKeyTypes type, double PIDKValue){
		if(type.getRoutingKeyPart().matches("\\.height\\.set[PID]")||type.getRoutingKeyPart().matches("\\.angle\\.set[PID]")){
			String lastRoutingKeyPart = type.getRoutingKeyPart();
			String firstRoutingKeyPart = "ijzer.private";
			String routingKey = firstRoutingKeyPart + lastRoutingKeyPart;

			String message= String.valueOf(PIDKValue);
			try {
				channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else throw new InvalidBindingKeyException("Het meegegeven type is verkeerd");
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
