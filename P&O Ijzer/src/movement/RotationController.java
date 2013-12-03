package movement;

import java.rmi.RemoteException;

import zeppelin.MainProgramImpl;
import QRCode.Orientation;
import client.ResultPointFinder;
import client.ResultPointFinderInterface;

import com.google.zxing.ResultPoint;

import controllers.MotorController;
import controllers.SensorController.TimeoutException;
import ftp.LogWriter;


public class RotationController {

	private MotorController motorController;
	private PIDController pController = new PIDController();
	private double safetyInterval = 5;
	private Orientation orientation;
	
	public RotationController(ResultPointFinderInterface finder) {
		this.orientation = new Orientation(finder);
	}

	public double getSafetyIntervalAngle() {
		return safetyInterval;
	}

	public void setSafetyIntervalAngle(double safetyInterval) {
		this.safetyInterval = safetyInterval;
	}

	private LogWriter logWriter;
	
	public void takeAction(double targetAngle) throws RemoteException, TimeoutException, InterruptedException {
		double pwm = 0;
		double mostRecentAngle = orientation.getOrientation();
		if(Math.abs(mostRecentAngle-targetAngle)> safetyInterval){
			pwm = this.getPWMValue(targetAngle, mostRecentAngle);
		}
		motorController.setTurnSpeed((int)pwm);
	}


	public double getPWMValue(double mostRecentAngle, double targetAngle) throws RemoteException, TimeoutException, InterruptedException {
		double pid = pController.takeAction(targetAngle, mostRecentAngle);
		return pid*0.1;
	}
	
	public boolean isInInterval(double mostRecentAngle, double targetAngle) {
		return Math.abs(mostRecentAngle-targetAngle)> safetyInterval;
	}

	public void setKpAngle(double kp) {
		this.pController.setKp(kp);
	}
	
	public void setKdAngle(double kd) {
		this.pController.setKd(kd);
	}
	
	public void setKiAngle(double ki) {
		this.pController.setKi(ki);
	}
	
	public double getKpAngle() {
		return pController.getKp();
	}
	
	public double getKdAngle() {
		return pController.getKd();
	}
	
	public double getKiAngle() {
		return pController.getKi();
	}



}

