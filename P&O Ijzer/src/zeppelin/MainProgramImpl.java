/**
 * Implementatie van ZeppelinInterface. Het is absoluut essentieel dat het programma dat op de Pi draait toegang
 * heeft tot deze klasse.
 */

package zeppelin;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;

import parser.Command;
import parser.Parser;
import movement.HeightAdjuster;
import movement.RotationController;
import QRCode.QRCodeHandler;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.RaspiPin;

import components.Motor;
import controllers.CameraController;
import controllers.MotorController;
import controllers.SensorController;
import controllers.SensorController.TimeoutException;
import ftp.FTPFileInfo;
import ftp.LogWriter;

public class MainProgramImpl extends UnicastRemoteObject implements MainProgramInterface {

	
	private static final long serialVersionUID = 1L;
	public static final GpioController gpio = GpioFactory.getInstance();
	
	/**
	 * Meest recente uitlezing van de sensor.
	 */
	private double mostRecentHeight;
	
	private double targetHeight;
	
	// Controllers
	public static final SensorController SENSOR_CONTROLLER = new SensorController(RaspiPin.GPIO_03, RaspiPin.GPIO_06);;
	public static final CameraController CAMERA_CONTROLLER = new CameraController();
	public static final MotorController MOTOR_CONTROLLER = new MotorController();
	public static final HeightAdjuster HEIGHT_ADJUSTER = new HeightAdjuster(MOTOR_CONTROLLER);
	public static final RotationController ROTATION_CONTROLLER = new RotationController();
	
	private Parser parser;
	
	private boolean qrCodeAvailable = false;
	
	public static final QRCodeHandler QR_CODE_READER = new QRCodeHandler();
	
	/**
	 * Meest recente string die de zeppelin heeft gedecodeerd uit een QR-code.
	 */
	private String mostRecentQRDecode;
	
	/**
	 * De zeppelin is nu bezig met landen en daarna de uitvoering te stoppen.
	 */
	private boolean exit = false;
	
	private LogWriter logWriter = new LogWriter();

	public MainProgramImpl() throws RemoteException {
		super();
		parser = new Parser(this);
		new Thread(new QrCodeLogicThread()).start();
		
		logWriter.writeToLog("------------ START NIEUWE SESSIE ------------- \n");
		
		try {
			this.targetHeight = SENSOR_CONTROLLER.sensorReading();
		} catch (TimeoutException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public double sensorReading() throws RemoteException {
		// TODO Auto-generated method stub
		return this.mostRecentHeight;
	}
	
	public double getTargetHeight() throws RemoteException {
		return this.targetHeight;
	}
	
	@Override
	public void setTargetHeight(double height) throws RemoteException {
		this.targetHeight = height;
	}
	
	private Parser getParser() {
		return this.parser;
	}
	
	private LogWriter getLogWriter() {
		return this.logWriter;
	}
	
	public void startGameLoop() throws InterruptedException {
		System.out.println("Lus initialiseren; zeppelin is klaar om commando's uit te voeren.");
		this.gameLoop();
		System.out.println("Lus afgebroken; uitvoering is stopgezet.");
	}
	
	public void exit() {
		this.exit = true;
	}
	
	/**
	 * Zolang de cliënt contact onderhoudt met de zeppelin, moet deze
	 * lus uitgevoerd worden. Om de beurt worden
	 * alle flags afgegaan en er wordt bepaald wat de zeppelin moet doen op basis
	 * van de status van die flags.
	 * 
	 * TODO: flags maken voor motoren, acties gebaseerd op deze flags implementeren
	 * @throws InterruptedException 
	 */
	private void gameLoop() throws InterruptedException {
		while (!exit) {
			try {
				this.mostRecentHeight = SENSOR_CONTROLLER.sensorReading();
				try {
					HEIGHT_ADJUSTER.takeAction(mostRecentHeight, targetHeight);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			} catch (TimeoutException e) {
				e.printStackTrace();
			}
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
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
	
	public void setKp(double kp) {
		HEIGHT_ADJUSTER.setKp(kp);
	}
	
	public void setKd(double kd) {
		HEIGHT_ADJUSTER.setKd(kd);
	}
	
	public void setKi(double ki) {
		HEIGHT_ADJUSTER.setKi(ki);
	}

	@Override
	public double getKp() throws RemoteException {
		return HEIGHT_ADJUSTER.getKp();
	}

	@Override
	public double getKi() throws RemoteException {
		return HEIGHT_ADJUSTER.getKd();
	}

	@Override
	public double getKd() throws RemoteException {
		return HEIGHT_ADJUSTER.getKi();
	}

	@Override
	public void setSafetyInterval(double safetyInterval) throws RemoteException {
		HEIGHT_ADJUSTER.setSafetyInterval(safetyInterval);
	}

	@Override
	public double getSafetyInterval() throws RemoteException {
		return HEIGHT_ADJUSTER.getSafetyInterval();
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
					MainProgramImpl.this.getLogWriter().writeToLog("QR-code gelezen met als resultaat: " + decoded);
					List<Command> commands = MainProgramImpl.this.getParser().parse(decoded);
					for (Command command: commands) {
						command.execute();
					}
				}
			}
			
		}
	}
}
