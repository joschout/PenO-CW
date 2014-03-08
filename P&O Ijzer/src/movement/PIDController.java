package movement;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;

import controllers.SensorController.TimeoutException;


/**
 * Deze klasse implementeert een PID-Controller
 * 
 * @invariant timeStamps.size() == errors.size()
 * @invariant timeStamp.size() <= amountOfData
 * 
 * 
 * @author Jonas
 *
 */
public class PIDController {

	int amountOfData = 2;
	
	double Kp;//Proportional gain
	double Ki;//Integral gain
	double Kd;//Derivative gain
	
	PIDMode mode;
	
	//Lijst met de tijdstippen waarop de
	private ArrayList<Double> timeStamps = new ArrayList<Double>();
	private ArrayList<Double> errors = new ArrayList<Double>();
	private double integral;
	
	
	public PIDController(double Kp, double Ki, double Kd, PIDMode mode) {
		this.Kp = Kp;
		this.Ki = Ki;
		this.Kd = Kd;
		this.mode = mode;
		this.integral = 0;
	}

	/**
	 * Berekent de fout tussen de twee meegegeven parameters.
	 * 
	 * @param targetValue de doelwaarde
	 * @param currentValue de echte gemeten waarde 
	 * @return de fout tussen de doelwaarde en de gemeten waarde
	 * 			| targetValue - currentValue
	 */
	private double calculateError(double targetValue, double currentValue) {
		if (this.mode == PIDMode.HEIGHT)
			return targetValue - currentValue;
		else // this.mode == PIDMode.ANGLE
			return RotationController.getAngle(currentValue, targetValue);
	}
	
	/**
	 * 
	 * @param targetValue de doelwaarde
	 * @param currentValue de echte gemeten waarde
	 * @throws RemoteException
	 * @throws TimeoutException
	 * @throws InterruptedException
	 */
	private void measureData(double targetValue, double currentValue) throws RemoteException, TimeoutException, InterruptedException {
		
		double error = calculateError(targetValue, currentValue);
		Date date = new Date();
		double time = date.getTime();
		addToLists(time, error);
	}
	
	/**
	 * Voegt de gegeven foutwaarde toe aan de lijst van fouten "errors".
	 * Voegt het gegeven tijdstip toe aan de lijst van tijdstippen "times".
	 * ALS de lijsten meer waardes bevatten dan "amountOfData", wordt er eerst een waarde verwijderd uit de lijsten.
	 * 
	 * MERK OP eigenlijk moeten er waardes blijven verwijderd worden tot de size < amountOfData. 
	 * 
	 * @param time een tijdstip
	 * @param error een foutwaarde
	 */
	private void addToLists(double time, double error){
		//methode implementeerd bound voor de linkedlist
		if (errors.size() >= amountOfData && timeStamps.size() >= amountOfData) {
			timeStamps.remove(0);
			errors.remove(0);
		}
		timeStamps.add(time);
		errors.add(error);
	}
	
	/**
	 * Berekent de numerieke eerste orde benadering van de AFGELEIDE
	 *  van de foutwaarden bij het laatste tijdstip in timeStamp.
	 *  
	 * @return null if errors.size() < 2
	 * 				else errors.get(size - 1) - errors.get(size - 2))/(timeStamps.get(size - 1) - timeStamps.get(size - 2)
	 */
	private Double differentiate() {
		int size = errors.size();
//		if(size >= 2) {
			return (errors.get(size - 1) - errors.get(size - 2))/(timeStamps.get(size - 1) - timeStamps.get(size - 2));
//		}
//		return null;
	}
	
	/**
	 * 
	 * @return
	 */
	private Double integrate() {
//		int i = 0;
		integral = integral + (timeStamps.get(errors.size() - 1) - timeStamps.get(errors.size() - 2))*(errors.get(errors.size() - 1) + errors.get(errors.size() - 2))/2;
		
//		while (i < errors.size() - 1) {
//			integral = integral + (timeStamps.get(i + 1) - timeStamps.get(i))*(errors.get(i + 1) + errors.get(i))/2;
//			i++;
//		}
		return integral;
	}
	
	/**
	 * 
	 * @return
	 */
	private Double getPID() {
		double proportion = Kp * errors.get(errors.size()-1);
		double integral = Ki * integrate();
		double derivative = Kd * differentiate();
		return proportion + integral + derivative;
	}
//	/**
//	 * 
//	 * @param mostRecentValue
//	 * @param targetValue
//	 * @return
//	 * @throws RemoteException
//	 * @throws TimeoutException
//	 * @throws InterruptedException
//	 */
//	public double getPWMValue(double mostRecentValue, double targetValue) throws RemoteException, TimeoutException, InterruptedException {
//		double pid = this.takeAction(targetValue, mostRecentValue);
//		return pid*0.1;
//	}
	/**
	 * 
	 * @param targetValue
	 * @param currentValue
	 * @return
	 * @throws RemoteException
	 * @throws TimeoutException
	 * @throws InterruptedException
	 */
	public Double getPIDValue(double targetValue, double currentValue) throws RemoteException, TimeoutException, InterruptedException {
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
