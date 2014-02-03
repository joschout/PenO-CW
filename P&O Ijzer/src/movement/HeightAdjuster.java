/**
 * Laat de zeppelin streven naar zijn doelhoogte met behulp van een PID-controller
 * die afstelt hoe sterk de naar-beneden-gerichte motor moet draaien.
 */

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
	 * Bepaalt een nieuwe PWM-waarde met behulp van de PID-controller.
	 * @param mostRecentValue
	 *        Meest recent gemeten hoogte van de zeppelin.
	 * @param targetValue
	 *        Doelhoogte van de zeppelin.
	 * @throws RemoteException
	 * @throws TimeoutException
	 * @throws InterruptedException
	 */
	public double getPWMValue(double mostRecentValue, double targetValue) throws RemoteException, TimeoutException, InterruptedException {
		double pid = pController.takeAction(targetValue, mostRecentValue);
		return pid*0.1;
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
	public void takeAction(double mostRecentHeight, double targetHeight) throws RemoteException, TimeoutException, InterruptedException {
		double pwm =0;
		if(Math.abs(mostRecentHeight-targetHeight)> safetyInterval){
			pwm = this.getPWMValue(targetHeight, mostRecentHeight);
		}
		motorController.setHeightSpeed((int) pwm);
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

	/**
	 * Zet de procesconstante.
	 */
	public void setKpHeight(double kp) {
		this.pController.setKp(kp);
	}
	
	/**
	 * Zet de derivative constante.
	 */
	public void setKdHeight(double kd) {
		this.pController.setKd(kd);
	}
	
	/**
	 * Zet de integraalconstante.
	 */
	public void setKiHeight(double ki) {
		this.pController.setKi(ki);
	}
	
	/**
	 * Haalt de procesconstante.
	 */
	public double getKpHeight() {
		return pController.getKp();
	}
	
	/**
	 * Haalt de derivative constante.
	 */
	public double getKdHeight() {
		return pController.getKd();
	}
	
	/**
	 * Haalt de integraalconstante.
	 */
	public double getKiHeight() {
		return pController.getKi();
	}
	
}
