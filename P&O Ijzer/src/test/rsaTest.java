package test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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
import qrcode.RSAInterface;
import qrcode.RSAWindows;
import zeppelin.MainProgramImpl;

public class rsaTest {

	public static final String EXCHANGE_NAME = "server";
	
	public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, IOException {

		RSAWindows rSA = new RSAWindows();

//		System.out.println(new BigInteger(rSA.getPublicKey().getEncoded()));
//		
		String string = "Milan is de beste";
		System.out.println("Input: " + string);
		rSA.encode(string);
		String encrypted = read("encrypted");
		
//		
//		System.out.println(encoded);
		String decoded = rSA.decode(encrypted);
		System.out.println(decoded);
		
		Connection connection = null;
		Channel channel = null;
		
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
		
		try {
			channel = connection.createChannel();
			channel.exchangeDeclare(EXCHANGE_NAME, "topic");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		channel.basicPublish(EXCHANGE_NAME, "ijzer.tablets.tablet1", null, rSA.getPublicKey().getBytes());
	}
	
	public static String read(String fileName){
		String result = "";
		try {
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				sb.append(System.lineSeparator());
				line = br.readLine();
			}
			result = sb.toString();
			br.close();
		} catch(IOException e) {
		}
		return result;
	}
	
	public static void write(String toWrite) {
		FileWriter fstream = null;
		BufferedWriter out = null;
		PrintWriter writer = null;
		
		File file = new File("encrypted");
		try {
			fstream = new FileWriter(file, false);
			out = new BufferedWriter(fstream);
			writer = new PrintWriter(out);
			writer.write(toWrite);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String decode() throws IOException {
		Runtime.getRuntime().exec("cmd /c python decription.py");
		String decryptedData = read("result");

		return decryptedData;
	}

}
