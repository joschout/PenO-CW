package movement;

import java.rmi.RemoteException;

import controllers.MotorController;
import controllers.SensorController.TimeoutException;
import ftp.LogWriter;

public class HeightAdjuster {
	
	private MotorController motorController;
	private PIDController pController = new PIDController();
	
	private LogWriter logWriter;
	
	public HeightAdjuster(MotorController motorController) {
		this.motorController = motorController;
		this.logWriter = new LogWriter();
	}
	
	public void takeAction(double mostRecentHeight, double targetHeight) throws RemoteException, TimeoutException, InterruptedException {
		double pwm = pController.getPWMValue(targetHeight, mostRecentHeight);
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
	
}
