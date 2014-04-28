package test;

import java.rmi.RemoteException;

import RabbitMQ.ZeppelinReceiver;
import zeppelin.MainProgramImpl;

public class RabbitMQTest {

	public static void main(String[] args) throws RemoteException {
		MainProgramImpl zeppelin = new MainProgramImpl();
		System.out.println("Receiver actief");
		while (true)
		{
			
		}

	}

}
