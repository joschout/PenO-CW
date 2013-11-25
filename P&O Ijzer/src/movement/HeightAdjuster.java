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
		
		//System.out.println("most recent height"+mostRecentHeight+", target height"+targetHeight+", pid value:"+pid);
	}
	
	
}
