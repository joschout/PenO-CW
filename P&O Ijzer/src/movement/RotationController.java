package movement;

import java.rmi.RemoteException;

import zeppelin.MainProgramImpl;
import QRCode.Orientation;
import client.FTPOrientation;
import client.FTPOrientationIface;

import com.google.zxing.ResultPoint;

import controllers.MotorController;
import controllers.SensorController.TimeoutException;
import ftp.LogWriter;


public class RotationController {

	private MotorController motorController;
	private PIDController pController = new PIDController(0.01, 0.001, 5, PIDMode.ANGLE); // TODO: constanten bepalen
	private double safetyInterval = 5;
	
	private MainProgramImpl zeppelin;

	public void setZeppelin(MainProgramImpl zeppelin) throws IllegalStateException {
		if (this.zeppelin != null)
			throw new IllegalStateException("Probeerde in rotation controller de zeppelin meer dan eens te zetten.");
		this.zeppelin = zeppelin;
	}
	
	public void setMotorController(MotorController controller) {
		if (this.motorController != null)
			throw new IllegalStateException("Probeerde in rotation controller de motor controller meer dan eens te zetten.");
		this.motorController = controller;
	}
	
	public double getSafetyIntervalAngle() {
		return safetyInterval;
	}

	public void setSafetyIntervalAngle(double safetyInterval) {
		this.safetyInterval = safetyInterval;
	}
	
	public void takeAction(double targetAngle, double zeppelinHeight) throws RemoteException, TimeoutException, InterruptedException {
		this.checkState();
		double pwm = 0;
		double mostRecentAngle = MainProgramImpl.ORIENTATION.getOrientation(zeppelinHeight);
		this.zeppelin.updateMostRecentAngle(mostRecentAngle);
		if(! isInInterval(mostRecentAngle, targetAngle)){
			pwm = this.getPWMValue(targetAngle, mostRecentAngle);
		}
		motorController.setTurnSpeed((int)pwm);
	}


	public double getPWMValue(double mostRecentAngle, double targetAngle) throws RemoteException, TimeoutException, InterruptedException {
		double pid = pController.takeAction(targetAngle, mostRecentAngle);
		return pid*0.05;
	}
	
	public boolean isInInterval(double mostRecentAngle, double targetAngle) {
		return Math.abs(mostRecentAngle-targetAngle) < safetyInterval;
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
	
	public static double convertToCorrectFormat(double angle) {
		double toReturn = angle % 360;
		if (angle < 0)
			return toReturn + 360;
		return toReturn;
	}
	
	public static double getAngle(double currentAngle, double targetAngle) {
		if(currentAngle == targetAngle) {
			return 0;
		}
		double leftAngle = (currentAngle + 360 - targetAngle) % 360;
		double rightAngle = (360 - currentAngle + targetAngle) % 360;
		if (Math.min(rightAngle, leftAngle) == leftAngle) {
			return -leftAngle;
		}
		else {
			return rightAngle;
		}
		
	}
	
	private void checkState() throws IllegalArgumentException {
		if (this.zeppelin == null)
			throw new IllegalArgumentException("Probeerde PID voor draaien te activeren, maar zeppelin was niet geïnstantieerd.");
		if (this.motorController == null)
			throw new IllegalArgumentException("Probeerde PID voor draaien te activeren, maar zeppelin was niet geïnstantieerd.");
	}



}

