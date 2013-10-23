package server;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import zeppelin.Zeppelin;

public class ZeppelinServer {
	
	public Zeppelin zeppelin;
	
	public ZeppelinServer(Zeppelin zeppelin) {
		this.zeppelin = zeppelin;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			System.setProperty("java.rmi.server.hostname", "192.168.2.100");
	    	LocateRegistry.createRegistry(1099);
	    	try {
				Naming.rebind("rmi://localhost:1099/Zeppelin", new Zeppelin());
				System.out.println("Server is ready.");
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (RemoteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

}
