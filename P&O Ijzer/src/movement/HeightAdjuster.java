package movement;

import java.rmi.RemoteException;

import controllers.MotorController;
import controllers.SensorController.TimeoutException;
import ftp.LogWriter;

public class HeightAdjuster {
	
	private MotorController motorController;
	private PIDController pController = new PIDController();
	private double safetyInterval = 1;
	
	public double getSafetyInterval() {
		return safetyInterval;
	}

	public void setSafetyInterval(double safetyInterval) {
		this.safetyInterval = safetyInterval;
	}

	private LogWriter logWriter;
	
	public HeightAdjuster(MotorController motorController) {
		this.motorController = motorController;
		this.logWriter = new LogWriter();
	}
	
	public void takeAction(double mostRecentHeight, double targetHeight) throws RemoteException, TimeoutException, InterruptedException {
		double pwm =0;
		if(Math.abs(mostRecentHeight-targetHeight)> safetyInterval){
			pwm = pController.getPWMValue(targetHeight, mostRecentHeight);
		}
		motorController.setSpeed((int) pwm);
		//System.out.println("most recent height"+mostRecentHeight+", target height"+targetHeight+", pid value:"+pid);
	}
	
	public void setKp(double kp) {
		this.pController.setKp(kp);
	}
	
	public void setKd(double kd) {
		this.pController.setKd(kd);
	}
	
	public void setKi(double ki) {
		this.pController.setKi(ki);
	}
	
	public double getKp() {
		return pController.getKp();
	}
	
	public double getKd() {
		return pController.getKd();
	}
	
	public double getKi() {
		return pController.getKi();
	}
	
}
