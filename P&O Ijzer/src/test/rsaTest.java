package test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Channel;

import coordinate.GridPoint;
import RabbitMQ.RabbitMQControllerZeppelin;
import qrcode.RSA;
import zeppelin.MainProgramImpl;

public class rsaTest {

	public static final String EXCHANGE_NAME = "server";
	
	public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, IOException {
		RSA rSA = new RSA();
		
//		System.out.println(new BigInteger(rSA.getPublicKey().getEncoded()));
//		
		String string = "Milan is de beste";
		rSA.encode(string);
//		
//		System.out.println(encoded);
		String decoded = rSA.decode(string);
		System.out.println(decoded);
		
		Connection connection = null;
		Channel channel = null;
		
		try {
		      ConnectionFactory factory = new ConnectionFactory();
		      factory.setHost("localhost");
//		      factory.setUsername("ijzer");
//		      factory.setPassword("ijzer");
		      factory.setPort(5672);
		  
		      connection = factory.newConnection();
		    }
		    catch  (Exception e) {
		      e.printStackTrace();
		    }
		
		try {
			channel = connection.createChannel();
			channel.exchangeDeclare(EXCHANGE_NAME, "topic");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		channel.basicPublish(EXCHANGE_NAME, "ijzer.tablets.tablet1", null, rSA.getPublicKey().getBytes());
	}

}
