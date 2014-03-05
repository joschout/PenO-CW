/**
 * Implementatie van ZeppelinInterface. Het is absoluut essentieel dat het programma dat op de Pi draait toegang
 * heeft tot deze klasse.
 */

package zeppelin;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import logger.LogWriter;
import movement.ForwardBackwardController;
import movement.HeightAdjuster;
import movement.RotationController;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.RaspiPin;

import controllers.CameraController;
import controllers.MotorController;
import controllers.SensorController;
import controllers.SensorController.TimeoutException;

public class MainProgramImpl extends UnicastRemoteObject implements MainProgramInterface {

	// LogWriter
	private static final long serialVersionUID = 1L;
	public static final GpioController gpio = GpioFactory.getInstance();

	/**
	 * Meest recente uitlezing van de sensor.
	 */
	private double mostRecentHeight;
	private double mostRecentAngle;

	private double targetHeight;
	private double targetAngle;

	// Controllers
	public static final SensorController SENSOR_CONTROLLER = new SensorController(RaspiPin.GPIO_03, RaspiPin.GPIO_06);;
	public static final CameraController CAMERA_CONTROLLER = new CameraController();
	public static final MotorController MOTOR_CONTROLLER = new MotorController();
	public static final HeightAdjuster HEIGHT_ADJUSTER = new HeightAdjuster(MOTOR_CONTROLLER);
	public static final RotationController ROTATION_CONTROLLER = new RotationController();
	public static final ForwardBackwardController FORWARD_BACKWARD = new ForwardBackwardController();

	// Dit object berekent de oriëntatie van de zeppelin

	public static final LogWriter LOG_WRITER = new LogWriter();

	private boolean qrCodeAvailable = false;

	/**
	 * Geeft aan of de zeppelin zijn activiteiten moet stopzetten.
	 */
	private boolean exit = false;

	public MainProgramImpl() throws RemoteException {
		super();
		ROTATION_CONTROLLER.setZeppelin(this);
		ROTATION_CONTROLLER.setMotorController(MOTOR_CONTROLLER);
		FORWARD_BACKWARD.setZeppelin(this);

		LOG_WRITER.writeToLog("------------ START NIEUWE SESSIE ------------- \n");

		try {
			this.targetHeight = SENSOR_CONTROLLER.sensorReading();
		} catch (TimeoutException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public double sensorReading() throws RemoteException {
		return this.mostRecentHeight;
	}

	public double getMostRecentAngle() {
		return this.mostRecentAngle;
	}

	public void updateMostRecentAngle(double angle) {
		this.mostRecentAngle = angle;
	}

	public double getTargetHeight() throws RemoteException {
		return this.targetHeight;
	}

	@Override
	public void setTargetHeight(double height) throws RemoteException {
		this.targetHeight = height;
	}

	public double getTargetAngle() {
		return this.targetAngle;
	}

	public void setTargetAngle(double targetAngle) {
		this.targetAngle = targetAngle;
	}

	public void startGameLoop() throws InterruptedException {
		System.out.println("Lus initialiseren; zeppelin is klaar om commando's uit te voeren.");
		this.gameLoop();
		System.out.println("Lus afgebroken; uitvoering is stopgezet.");
	}

	public void exit() {
		this.exit = true;
	}

	private boolean turning = false;

	public void setTurning(boolean value) {
		this.turning = value;
	}

	/**
	 * Zolang de cliënt contact onderhoudt met de zeppelin, moet deze
	 * lus uitgevoerd worden. In elke iteratie wordt er actie genomen om
	 * de huidige doelhoogte en huidige doelhoek te bereiken.
	 *
	 * @throws InterruptedException 
	 */
	private void gameLoop() throws InterruptedException {
		while (!exit) {
			try {
				this.mostRecentHeight = SENSOR_CONTROLLER.sensorReading();
				try {
					HEIGHT_ADJUSTER.takeAction(mostRecentHeight, targetHeight);
					if (turning) {
						System.out.println("Huidige hoek van zeppelin: " + mostRecentAngle + " met timestamp: " + System.currentTimeMillis());
						System.out.println("Zeppelin: In if-test met target angle: " + targetAngle);
						ROTATION_CONTROLLER.takeAction(targetAngle, mostRecentHeight);
					}
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			} catch (TimeoutException e) {

			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		MOTOR_CONTROLLER.writeSoftPwmValues(0, 0, 0, 0);
		MOTOR_CONTROLLER.stopRightAndLeftMotor();
		MOTOR_CONTROLLER.stopHeightAdjustment();
		System.exit(0);
	}

	public boolean qrCodeAvailable() throws RemoteException {
		return this.qrCodeAvailable;
	}

	public void qrCodeConsumed() throws RemoteException {
		this.qrCodeAvailable = false;
	}

	@Override
	public boolean leftIsOn() throws RemoteException {
		return MOTOR_CONTROLLER.leftIsOn();
	}

	@Override
	public boolean rightIsOn() throws RemoteException {
		return MOTOR_CONTROLLER.rightIsOn();
	}

	@Override
	public boolean downwardIsOn() throws RemoteException {
		return MOTOR_CONTROLLER.downwardIsOn();
	}

	@Override
	public void goForward() throws RemoteException {
		MOTOR_CONTROLLER.forward();

	}

	@Override
	public void goBackward() throws RemoteException {
		MOTOR_CONTROLLER.backward();
	}

	@Override
	public void turnLeft() throws RemoteException {
		MOTOR_CONTROLLER.clientLeft();
	}

	@Override
	public void turnRight() throws RemoteException {
		MOTOR_CONTROLLER.clientRight();
	}

	@Override
	public void stopRightAndLeft() throws RemoteException {
		MOTOR_CONTROLLER.stopRightAndLeftMotor();
	}

	public void stopDownward() throws RemoteException {
		MOTOR_CONTROLLER.stopHeightAdjustment();
	}

	@Override
	public void setKpHeight(double kp) {
		HEIGHT_ADJUSTER.setKpHeight(kp);
	}

	@Override
	public void setKdHeight(double kd) {
		HEIGHT_ADJUSTER.setKdHeight(kd);
	}

	@Override
	public void setKiHeight(double ki) {
		HEIGHT_ADJUSTER.setKiHeight(ki);
	}

	@Override
	public double getKpHeight() throws RemoteException {
		return HEIGHT_ADJUSTER.getKpHeight();
	}

	@Override
	public double getKiHeight() throws RemoteException {
		return HEIGHT_ADJUSTER.getKdHeight();
	}

	@Override
	public double getKdHeight() throws RemoteException {
		return HEIGHT_ADJUSTER.getKiHeight();
	}

	@Override
	public void setSafetyIntervalHeight(double safetyInterval) throws RemoteException {
		HEIGHT_ADJUSTER.setSafetyIntervalHeight(safetyInterval);
	}

	@Override
	public double getSafetyIntervalHeight() throws RemoteException {
		return HEIGHT_ADJUSTER.getSafetyIntervalHeight();
	}

	@Override
	public void setKpAngle(double kp) {
		ROTATION_CONTROLLER.setKpAngle(kp);
	}

	@Override
	public void setKdAngle(double kd) {
		ROTATION_CONTROLLER.setKdAngle(kd);
	}

	@Override
	public void setKiAngle(double ki) {
		ROTATION_CONTROLLER.setKiAngle(ki);
	}

	@Override
	public double getKpAngle() throws RemoteException {
		return ROTATION_CONTROLLER.getKpAngle();
	}

	@Override
	public double getKiAngle() throws RemoteException {
		return ROTATION_CONTROLLER.getKdAngle();
	}

	@Override
	public double getKdAngle() throws RemoteException {
		return ROTATION_CONTROLLER.getKiAngle();
	}

	@Override
	public void setSafetyIntervalAngle(double safetyInterval) throws RemoteException {
		ROTATION_CONTROLLER.setSafetyIntervalAngle(safetyInterval);
	}

	@Override
	public double getSafetyIntervalAngle() throws RemoteException {
		return ROTATION_CONTROLLER.getSafetyIntervalAngle();
	}

	@Override
	public String readLog() throws RemoteException {
		return LOG_WRITER.getLog();
	}

}
