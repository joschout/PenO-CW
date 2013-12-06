package movement;

import java.rmi.RemoteException;

import controllers.MotorController;
import controllers.SensorController.TimeoutException;
import ftp.LogWriter;

public class HeightAdjuster {
	
	private MotorController motorController;
	private PIDController pController = new PIDController(17, 0.001, 13500, PIDMode.HEIGHT);
	private double safetyInterval = 1;
	
	public double getSafetyIntervalHeight() {
		return safetyInterval;
	}

	public void setSafetyIntervalHeight(double safetyInterval) {
		this.safetyInterval = safetyInterval;
	}
	
	public HeightAdjuster(MotorController motorController) {
		this.motorController = motorController;
	}
	
	
	/**
	 * 
	 * @param mostRecentValue
	 * @param targetValue
	 * @return
	 * @throws RemoteException
	 * @throws TimeoutException
	 * @throws InterruptedException
	 */
	public double getPWMValue(double mostRecentValue, double targetValue) throws RemoteException, TimeoutException, InterruptedException {
		double pid = pController.takeAction(targetValue, mostRecentValue);
		return pid*0.1;
	}
	
	
	
	public void takeAction(double mostRecentHeight, double targetHeight) throws RemoteException, TimeoutException, InterruptedException {
		double pwm =0;
		if(Math.abs(mostRecentHeight-targetHeight)> safetyInterval){
			pwm = this.getPWMValue(targetHeight, mostRecentHeight);
		}
		motorController.setHeightSpeed((int) pwm);
		//System.out.println("most recent height"+mostRecentHeight+", target height"+targetHeight+", pid value:"+pid);
	}

	public boolean isInInterval(double mostRecentHeight, double targetHeight) {
		return Math.abs(mostRecentHeight-targetHeight)> safetyInterval;
	}

	public void setKpHeight(double kp) {
		this.pController.setKp(kp);
	}
	
	public void setKdHeight(double kd) {
		this.pController.setKd(kd);
	}
	
	public void setKiHeight(double ki) {
		this.pController.setKi(ki);
	}
	
	public double getKpHeight() {
		return pController.getKp();
	}
	
	public double getKdHeight() {
		return pController.getKd();
	}
	
	public double getKiHeight() {
		return pController.getKi();
	}
	
}
