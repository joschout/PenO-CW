package movement;

import java.rmi.RemoteException;
import java.util.LinkedList;

import com.pi4j.io.gpio.RaspiPin;

import controllers.SensorController;
import controllers.SensorController.TimeoutException;
import zeppelin.ZeppelinInterface;

public class PIDController {

	private SensorController sensor = new SensorController(RaspiPin.GPIO_03, RaspiPin.GPIO_06);
	private ZeppelinInterface zeppelin;
	
	int amountOfData = 5;
	
	double Kp;//Proportional gain
	double Ki;//Integral gain
	double Kd;//Derivative gain
	
	private LinkedList<Double> errors = new LinkedList<Double>();
	
	private double calculateError(double targetHeight, double currentHeight) {
		return targetHeight - currentHeight;
	}
	
	private LinkedList measureData() throws RemoteException, TimeoutException, InterruptedException {
		double targetHeight = zeppelin.getTargetHeight();
		int i = 0;
		while (i < amountOfData) {
			double currentHeight = sensor.sensorReading();
			double error = calculateError(targetHeight, currentHeight);
			addError(error);
		}
			

		
	}
	
	private void addError(double error){
		//methode implementeerd bound voor de linkedlist
	}
	
	private void calculateErrorFunction() {
		
		
	}
	
	
}
