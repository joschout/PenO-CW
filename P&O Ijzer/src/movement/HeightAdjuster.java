package movement;

import controllers.MotorController;
import controllers.SensorController;
import ftp.LogWriter;

public class HeightAdjuster {
	
	private MotorController motorController;
	
	private double delta = 10;
	private double safetyInterval1 = 15;
	private double safetyInterval2 = 5;
	
	private LogWriter logWriter;
	
	public HeightAdjuster(MotorController motorController) {
		this.motorController = motorController;
		this.logWriter = new LogWriter();
	}
	
	public void takeAction(double mostRecentHeight, double targetHeight) {
		if (isInInterval(mostRecentHeight, targetHeight)) {
//			logWriter.writeToLog("Zeppelin zweeft binnen aanvaardbare marge op doelhoogte: " + targetHeight);
			motorController.stopHeightAdjustment();
			return;	
		}
		if (mostRecentHeight < targetHeight - delta) {
//			logWriter.writeToLog("Zeppelin zweeft onder de doelhoogte; laat zeppelin stijgen.");
			motorSpeedAdjustment(mostRecentHeight, targetHeight);
			motorController.up();	
		}
		if (mostRecentHeight > targetHeight + delta) {
//			logWriter.writeToLog("Zeppelin zweeft boven de doelhoogte; laat zeppelin dalen.");
			motorSpeedAdjustment(mostRecentHeight, targetHeight);
			motorController.down();
		}
	}
	
	private boolean isInInterval(double height, double targetHeight) {
		return (targetHeight - delta <= height) && (height <= targetHeight + delta);
	}
	
	private void motorSpeedAdjustment(double height, double targetHeight) {
		/*
		 * abs(height-tqrget_height) < interval
		 * 
		 * (height >= targetHeight - delta - safetyInterval2) ||
				height <= targetHeight + delta + safetyInterval2
		 */
		if (Math.abs(height - targetHeight) <= (delta + safetyInterval2)) {
//			logWriter.writeToLog("Zeppelin zweeft op 5cm af van doelhoogte; motor op halve kracht laten draaien.");
			MotorController.setSpeed(50);
		}
		else if (Math.abs(height - targetHeight) <= (delta + safetyInterval1)) {
//			logWriter.writeToLog("Zeppelin zweeft op 15 cm af van doelhoogte; motor op 80 procent kracht laten draaien.");
			MotorController.setSpeed(80);
		}
		else MotorController.setSpeed(100);
	}
}
