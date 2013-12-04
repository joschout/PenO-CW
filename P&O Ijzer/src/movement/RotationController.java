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
	private PIDController pController = new PIDController(17, 0.001, 13500, PIDMode.ANGLE); // TODO: constanten bepalen
	private double safetyInterval = 5;
	
	private MainProgramImpl zeppelin;

	public void setZeppelin(MainProgramImpl zeppelin) throws IllegalStateException {
		if (this.zeppelin != null)
			throw new IllegalStateException("Probeerde in rotation controller de zeppelin meer dan eens te zetten.");
	}
	
	public double getSafetyIntervalAngle() {
		return safetyInterval;
	}

	public void setSafetyIntervalAngle(double safetyInterval) {
		this.safetyInterval = safetyInterval;
	}

	private LogWriter logWriter;
	
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
		return pid*0.1;
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
	
	public static double getAngleComplement(double angle) {
		return 360 - angle;
	}
	
	/**
	 * Neem als conventie dat 
	 * @param angle
	 * @param otherAngle
	 * @return
	 */
	public static double getLeftTurnDistance(double angle, double otherAngle) {
		if (angle > otherAngle)
			return Math.abs(- angle - otherAngle);
		return Math.abs(angle - otherAngle);
	}
	
	public static double getRightTurnDistance(double angle, double otherAngle) {
		if (otherAngle > angle)
			return Math.abs(- angle - getAngleComplement(otherAngle));
		return Math.abs(angle - otherAngle);
	}
	
	private void checkState() throws IllegalArgumentException {
		if (this.zeppelin == null)
			throw new IllegalArgumentException("Probeerde PID voor draaien te activeren, maar zeppelin was niet geïnstantieerd.");
	}



}

