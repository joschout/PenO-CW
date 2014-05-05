/**
 * Laat de zeppelin streven naar zijn doelhoogte met behulp van een PID-controller
 * die afstelt hoe sterk de naar-beneden-gerichte motor moet draaien.
 */

package movement;

import java.io.Serializable;


import controllers.MotorController;
import controllers.SensorController;
import controllers.SensorController.TimeoutException;

public class HeightController implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private MotorController motorController;
	private PIDController pController = new PIDController(0.5, 0, 70, PIDMode.HEIGHT);
	private double safetyInterval = 1;
	private SensorController sensorController;
	
	private double mostRecentHeight;
	
	public HeightController(SensorController sensorController, MotorController motorController) {
		this.motorController = motorController;
		this.sensorController = sensorController;
	}
	
	public double getSafetyIntervalHeight() {
		return safetyInterval;
	}

	public void setSafetyIntervalHeight(double safetyInterval) {
		this.safetyInterval = safetyInterval;
	}
	
	/**
	 * Bepaalt een nieuwe PWM-waarde met behulp van de PID-controller.
	 * @param mostRecentValue
	 *        Meest recent gemeten hoogte van de zeppelin.
	 * @param targetValue
	 *        Doelhoogte van de zeppelin.
	 * @throws RemoteException
	 * @throws TimeoutException
	 * @throws InterruptedException
	 */

	public double getPWMValue(double mostRecentValue, double targetValue) throws TimeoutException, InterruptedException {

		double pid = pController.getPIDValue(targetValue, mostRecentValue);
		return pid;
	}
	
	
	/**
	 * Voert een iteratie van het controleproces uit voor de gegeven gemeten hoogte en doelhoogte.
	 * @param mostRecentHeight
	 *        Meest recent gemeten hoogte van de zeppelin.
	 * @param targetHeight
	 *        Doelhoogte van de zeppelin.
	 * @throws RemoteException
	 * @throws TimeoutException
	 * @throws InterruptedException
	 */

	public void goToHeight(double targetHeight) throws TimeoutException, InterruptedException {
		double pwm = -39;
		
		double mostRecentHeight = sensorController.sensorReading();
		
		if (mostRecentHeight >= 1.5 * this.mostRecentHeight) {
			this.setHeight(this.mostRecentHeight);
		} else {
			this.setHeight(mostRecentHeight);
		}

		if(Math.abs(mostRecentHeight-targetHeight) > safetyInterval){
			pwm = this.getPWMValue(targetHeight, mostRecentHeight) - 39;
		}
		motorController.setHeightSpeed((int) pwm);
		System.out.println("PWM-waarde nieuwe iteratie: " + pwm);
		//System.out.println("most recent height"+mostRecentHeight+", target height"+targetHeight+", pid value:"+pid);
	}

	/**
	 * Bepaalt of de gegeven gemeten hoogte in het veiligheidsinterval zit rond de gegeven doelhoogte.
	 * @param mostRecentHeight
	 *        Meest recent gemeten hoogte van de zeppelin.
	 * @param targetHeight
	 *        Doelhoogte van de zeppelin.
	 * @return
	 */
	public boolean isInInterval(double mostRecentHeight, double targetHeight) {
		return Math.abs(mostRecentHeight-targetHeight) <= safetyInterval;
	}

	public PIDController getpController() {
		return pController;
	}

	public void setHeight(double height) {
		this.mostRecentHeight = height;
	}
	
	public double getHeight() {
		return this.mostRecentHeight;
	}
	
}
