/**
 * Automatische regeling van het draaien. Bepaalt hoe sterk de linker- en rechtermotoren
 * moeten draaien met behulp van een PID controller.
 */

package movement;

//import java.rmi.RemoteException;
import zeppelin.MainProgramImpl;
import controllers.MotorController;
import controllers.SensorController.TimeoutException;


public class RotationController {
	
	private MotorController motorController;
	private PIDController pController = new PIDController(0.01, 0.001, 5, PIDMode.ANGLE);
	private double safetyInterval = 5;
	
	private MainProgramImpl zeppelin;
	
	private TurnSpeedCalculator calc;
	

	public RotationController(MainProgramImpl zeppelin, MotorController motorcontroller) {
		this.zeppelin = zeppelin;
		this.motorController = motorcontroller;
		this.calc = new TurnSpeedCalculator();
	}

	public MainProgramImpl getZeppelin() {
		return this.zeppelin;
	}
	
	public TurnSpeedCalculator getSpeedCalculator() {
		return this.calc;
	}
	
	public void setZeppelin(MainProgramImpl zeppelin) throws IllegalStateException {
		if (this.zeppelin != null)
			throw new IllegalStateException("Probeerde in rotation controller de zeppelin meer dan eens te zetten.");
		this.zeppelin = zeppelin;
	}
	
	
	
	public double getSafetyIntervalAngle() {
		return safetyInterval;
	}

	public void setSafetyIntervalAngle(double safetyInterval) {
		this.safetyInterval = safetyInterval;
	}
	
	public void goToAngle() throws  InterruptedException, TimeoutException {
		this.checkState();
		int pwm = 0;
		
		double angleError = this.getZeppelin().getAngleError();
		//this.zeppelin.updateMostRecentAngle(mostRecentAngle); TODO OBSOLETE?
		if(! isInInterval(angleError)){
			pwm = this.getSpeedCalculator().calculateTurnSpeed(angleError);
		}
		motorController.setTurnSpeed(pwm);
	}

	public double getPWMValue(double mostRecentAngle, double targetAngle) throws  TimeoutException, InterruptedException {
		double pid = pController.getPIDValue(targetAngle, mostRecentAngle);
		return pid;
	}
	
	public boolean isInInterval(double angleError) {
		return Math.abs(angleError) < safetyInterval;
	}

	public PIDController getpController() {
		return pController;
	}
	
	
	public static double convertToCorrectFormat(double angle) {
		double toReturn = angle % 360;
		if (angle < 0)
			return toReturn + 360;
		return toReturn;
	}
	
	/**
	 * Berekent de fout voor de gegeven gemeten hoek en de gegeven doelhoek.
	 * @param currentAngle
	 *        Gemeten hoek.
	 * @param targetAngle
	 *        Doelhoek.
	 */
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

