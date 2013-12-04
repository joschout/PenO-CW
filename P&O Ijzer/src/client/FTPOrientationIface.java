package client;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface FTPOrientationIface extends Remote {
	
	public double findOrientationFromPicture(String filename) throws RemoteException;
}
