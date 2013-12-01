package movement;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;

import com.pi4j.io.gpio.RaspiPin;

import controllers.SensorController;
import controllers.SensorController.TimeoutException;
import zeppelin.ZeppelinInterface;

public class PIDController {

	int amountOfData = 5;
	
	double Kp = 17;//Proportional gain
	double Ki = 0.001;//Integral gain
	double Kd = 13500;//Derivative gain
	
	private ArrayList<Double> times = new ArrayList<Double>();
	private ArrayList<Double> errors = new ArrayList<Double>();
	

	private double calculateError(double targetValue, double currentValue) {
		return targetValue - currentValue;
	}
	
	private void measureData(double targetValue, double currentValue) throws RemoteException, TimeoutException, InterruptedException {
		double error = calculateError(targetValue, currentValue);
		Date date = new Date();
		double time = date.getTime();
		addError(time, error);
	}
	
	private void addError(double time, double error){
		//methode implementeerd bound voor de linkedlist
		if (errors.size() >= amountOfData && times.size() >= amountOfData) {
			times.remove(0);
			errors.remove(0);
		}
		times.add(time);
		errors.add(error);
	}
	
	private Double differentiate() {
		int size = errors.size();
		if(size >= 2) {
			
			return (errors.get(size - 1) - errors.get(size - 2))/(times.get(size - 1) - times.get(size - 2));
		}
		return null;
	}
	
	private Double integrate() {
		int i = 0;
		double integral = 0;
		while (i < errors.size() - 1) {
			integral = integral + (times.get(i + 1) - times.get(i))*(errors.get(i + 1) + errors.get(i))/2;
			i++;
		}
		return integral;
	}
	
	private Double getPID() {
		double proportion = Kp * errors.get(errors.size()-1);
		double integral = Ki * integrate();
		double derivative = Kd * differentiate();
		return proportion + integral + derivative;
	}
	
	public double getPWMValue(double mostRecentValue, double targetValue) throws RemoteException, TimeoutException, InterruptedException {
		double pid = this.takeAction(targetValue, mostRecentValue);
		return pid*0.1;
	}
	
	public Double takeAction(double targetValue, double currentValue) throws RemoteException, TimeoutException, InterruptedException {
		measureData(targetValue, currentValue);
		if(errors.size() >= 2 && !(Kp == 0 && Ki == 0 && Kd == 0)) {
			return getPID();
		}
		else {
			return 0.0;
		}
	}

	public double getKp() {
		return Kp;
	}

	public void setKp(double kp) {
		this.Kp = kp;
	}

	public double getKi() {
		return Ki;
	}

	public void setKi(double ki) {
		this.Ki = ki;
	}

	public double getKd() {
		return Kd;
	}

	public void setKd(double kd) {
		this.Kd = kd;
	}
	
}
