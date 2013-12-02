package movement;

import java.rmi.RemoteException;

import zeppelin.MainProgramImpl;

import com.google.zxing.ResultPoint;

import controllers.MotorController;
import controllers.SensorController.TimeoutException;

import ftp.LogWriter;


public class RotationController {

	private MotorController motorController;
	private PIDController pController = new PIDController();
	private double safetyInterval = 5;

	public double getSafetyIntervalAngle() {
		return safetyInterval;
	}

	public void setSafetyIntervalAngle(double safetyInterval) {
		this.safetyInterval = safetyInterval;
	}

	private LogWriter logWriter;

	public double calcRotation(double zeppelinHeight, ResultPoint[] points) {
		String filename = Long.toString(System.currentTimeMillis());
		points = MainProgramImpl.QR_CODE_READER.findResultPoints(zeppelinHeight, filename);
		ResultPoint a= points[1];
		ResultPoint b= points[2];
		ResultPoint c= points[0];
		//Find the degree of the rotation that is needed

		double x = c.getX()-a.getX();
		double y = c.getY()-a.getY();
		double theta = Math.toDegrees(Math.atan2(x, -y));
		theta += 180;
		if(theta < 0)
			theta += 360;
		return theta;
	}
	
	public void takeAction(double mostRecentAngle, double targetAngle) throws RemoteException, TimeoutException, InterruptedException {
		double pwm =0;
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

