package zeppelin;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

public interface ZeppelinInterface extends Remote{
	
	public void activateDownwardMotor() throws RemoteException;
	
	public void activateLeftMotor() throws RemoteException;
	
	public void activateRightMotor() throws RemoteException;
	
	public void activateBackwardMotor() throws RemoteException;
	
	public double sensorReading() throws RemoteException;
	
	public Map<String,String> queryState() throws RemoteException;

}
