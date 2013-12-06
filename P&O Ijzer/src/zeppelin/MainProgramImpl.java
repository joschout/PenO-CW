/**
 * Implementatie van ZeppelinInterface. Het is absoluut essentieel dat het programma dat op de Pi draait toegang
 * heeft tot deze klasse.
 */

package zeppelin;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import parser.Command;
import parser.Parser;
import movement.HeightAdjuster;
import movement.RotationController;
import QRCode.Orientation;
import QRCode.QRCodeHandler;
import client.FTPOrientationIface;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.RaspiPin;

import controllers.CameraController;
import controllers.MotorController;
import controllers.SensorController;
import controllers.SensorController.TimeoutException;
import ftp.LogWriter;

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
	
	// Dit object berekent de oriëntatie van de zeppelin
	public static final Orientation ORIENTATION = new Orientation();
	
	public static final LogWriter LOG_WRITER = new LogWriter();
	
	private Parser parser;
	
	private boolean qrCodeAvailable = false;
	
	public static final QRCodeHandler QR_CODE_READER = new QRCodeHandler();
	
	/**
	 * De zeppelin is nu bezig met landen en daarna de uitvoering te stoppen.
	 */
	private boolean exit = false;

	public MainProgramImpl() throws RemoteException {
		super();
		parser = new Parser(this);
		ROTATION_CONTROLLER.setZeppelin(this);
		ROTATION_CONTROLLER.setMotorController(MOTOR_CONTROLLER);
		
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
	
	private Parser getParser() {
		return this.parser;
	}
	
	public void startGameLoop() throws InterruptedException {
		System.out.println("Wachten op beschikbare client voor meten van hoek op basis van QR-codes");
		while (! ORIENTATION.finderSet()) {
			Thread.sleep(500);
		}
		new Thread(new QrCodeLogicThread()).start();
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
	 * lus uitgevoerd worden. Om de beurt worden
	 * alle flags afgegaan en er wordt bepaald wat de zeppelin moet doen op basis
	 * van de status van die flags.
	 *
	 * @throws InterruptedException 
	 */
	private void gameLoop() throws InterruptedException {
		while (!exit) {
			System.out.println("Aan het draaien: " + turning);
			try {
				this.mostRecentHeight = SENSOR_CONTROLLER.sensorReading();
				try {
					HEIGHT_ADJUSTER.takeAction(mostRecentHeight, targetHeight);
					if (turning) {
						System.out.println("Huidige hoek van zeppelin: " + mostRecentAngle);
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
	
	private void offerQrCode() {
		this.qrCodeAvailable = true;
	}
	
	public void notifyClientAvailable() {
		try {
			Registry registry = LocateRegistry.getRegistry(java.rmi.server.RemoteServer.getClientHost(), 1099);
			FTPOrientationIface finder = 
					(FTPOrientationIface) registry.lookup("Finder");
			System.out.println("Deze finder wordt gezet in Orientation: " + finder);
			ORIENTATION.setFinder(finder);
			
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (ServerNotActiveException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public String readNewQRCode() throws RemoteException, IOException, InterruptedException {
		return QR_CODE_READER.tryReadQrCode(this.sensorReading());
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
		MOTOR_CONTROLLER.left();
	}

	@Override
	public void turnRight() throws RemoteException {
		MOTOR_CONTROLLER.right();
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

	/**
	 * Staat in voor het constant zoeken naar een nieuwe QR-code en
	 * het uitvoeren van instructies geëncodeerd in een QR-code
	 * @author Thomas
	 *
	 */
	private class QrCodeLogicThread implements Runnable {
		
		/**
		 * - Kan een QR-code vinden?
		 * 		-> nee: slaap een seconde lang, repeat
		 * - Parse QR-code
		 * - Voer commando's uit
		 * - repeat
		 */
		public void run() {
			while (true) {
				String decoded = null;
				try {
					decoded = MainProgramImpl.this.readNewQRCode();
				} catch (RemoteException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (decoded == null) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				else {
					MainProgramImpl.this.offerQrCode();
					LOG_WRITER.writeToLog("QR-code gelezen met als resultaat: " + decoded);
					List<Command> commands = MainProgramImpl.this.getParser().parse(decoded);
					for (Command command: commands) {
						command.execute();
					}
				}
			}
			
		}
	}

}
