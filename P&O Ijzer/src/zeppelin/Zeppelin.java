package zeppelin;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;

import com.pi4j.io.gpio.RaspiPin;

import controllers.SensorController;
import controllers.SensorController.TimeoutException;

public class Zeppelin extends UnicastRemoteObject implements ZeppelinInterface {

	
	private static final long serialVersionUID = 1L;

	public Zeppelin() throws RemoteException {
		super();
		sensorController = new SensorController(RaspiPin.GPIO_00, RaspiPin.GPIO_07);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void activateDownwardMotor() throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void activateLeftMotor() throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void activateRightMotor() throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void activateBackwardMotor() throws RemoteException {
		// TODO Auto-generated method stub

	}

	private SensorController sensorController;
	
	@Override
	public double sensorReading() throws RemoteException {
		// TODO Auto-generated method stub
		try {
			return sensorController.sensorReading();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public Map<String, String> queryState() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

}
