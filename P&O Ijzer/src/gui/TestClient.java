package gui;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import zeppelin.ZeppelinInterface;

public class TestClient {
	
	public static double sensorReading() {
		try {
			Registry registry = LocateRegistry.getRegistry("192.168.2.100",1099);
			ZeppelinInterface zeppelin = null;
			try {
				zeppelin = (ZeppelinInterface) registry.lookup ("Zeppelin");
			} catch (NotBoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return zeppelin.sensorReading();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

}
