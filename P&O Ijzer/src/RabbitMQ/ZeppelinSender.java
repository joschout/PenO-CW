package RabbitMQ;

import java.io.IOException;

import logger.LogWriter;
import zeppelin.MainProgramImpl;
import logger.LogWriter;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public class ZeppelinSender {

	private MessageParser parser;
	private MainProgramImpl zeppelin;
	private Channel channel;
	private static final String EXCHANGE_NAME = "server";

	public ZeppelinSender(MainProgramImpl zeppelin, Connection connection) {
		this.initialiseChannel(connection);
		this.zeppelin = zeppelin;
	}

	public MainProgramImpl getZeppelin() {
		return this.zeppelin;
	}

	public Channel getChannel() {
		return this.channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public void sendLocation(){
		double xInCM = this.getZeppelin().getPosition().x;
		double yInCM = this.getZeppelin().getPosition().y;

		int xInMM = (int)xInCM*100;
		int yInMM = (int)yInCM*100;

		String xAsString = String.valueOf(xInMM);
		String yAsString = String.valueOf(yInMM);

		String routingKey = "ijzer.info.location";
		String message = xAsString + "," +yAsString;

		try {
			channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendHeight(){
		double heightInCm = this.getZeppelin().getHeight();
		int heightInMM = (int)heightInCm*100;

		String routingKey = "ijzer.info.height";
		String message = String.valueOf(heightInMM);
		try {
			channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void sendPrivateMessage(PrivateRoutingKeyTypes type){
		String lastRoutingKeyPart = type.getRoutingKeyPart();
		String firstRoutingKeyPart = "ijzer.private";
		String routingKey = firstRoutingKeyPart + lastRoutingKeyPart;

		String message;
		try {
			switch(type){
			case LOG:
				message = LogWriter.INSTANCE.getLog();
				channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes());
			case PID_ANGLE_GETP:
				double KpAngle =this.zeppelin.getRotationController().getpController().getKp();
				message = String.valueOf(KpAngle);
				channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes());
			case PID_ANGLE_GETI:
				double KiAngle =this.zeppelin.getRotationController().getpController().getKi();
				message = String.valueOf(KiAngle);
				channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes());
			case PID_ANGLE_GETD:
				double KdAngle =this.zeppelin.getRotationController().getpController().getKd();
				message = String.valueOf(KdAngle);
				channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes());
			case PID_ANGLE_SETP:
				System.out.println("Een zeppelin kan de Kp-waarde van zijn rotationController intern aanpassen; dit heeft geen nut over de server.");	
			case PID_ANGLE_SETI:
				System.out.println("Een zeppelin kan de Ki-waarde van zijn rotationController intern aanpassen; dit heeft geen nut over de server.");
			case PID_ANGLE_SETD:	
				System.out.println("Een zeppelin kan de Kd-waarde van zijn rotationController intern aanpassen; dit heeft geen nut over de server.");


			case PID_HEIGHT_GETP:
				double KpHeight = this.getZeppelin().getHeightController().getpController().getKp();
				message = String.valueOf(KpHeight);
				channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes());
			case PID_HEIGHT_GETI:
				double KiHeight = this.getZeppelin().getHeightController().getpController().getKi();
				message = String.valueOf(KiHeight);
				channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes());
			case PID_HEIGHT_GETD:
				double KdHeight = this.getZeppelin().getHeightController().getpController().getKd();
				message = String.valueOf(KdHeight);
				channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes());
			case PID_HEIGHT_SETP:
				System.out.println("Een zeppelin kan de Kp-waarde van zijn heightController intern aanpassen; dit heeft geen nut over de server.");
			case PID_HEIGHT_SETI:
				System.out.println("Een zeppelin kan de Ki-waarde van zijn heightController intern aanpassen; dit heeft geen nut over de server.");
			case PID_HEIGHT_SETD:
				System.out.println("Een zeppelin kan de Kd-waarde van zijn heightController intern aanpassen; dit heeft geen nut over de server.");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

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
