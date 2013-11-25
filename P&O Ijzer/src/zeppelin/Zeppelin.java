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

import movement.HeightAdjuster;
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

public class Zeppelin extends UnicastRemoteObject implements ZeppelinInterface {

	
	private static final long serialVersionUID = 1L;
	public static final GpioController gpio = GpioFactory.getInstance();
	
	/**
	 * Meest recente uitlezing van de sensor.
	 */
	private double mostRecentHeight;
	
	private double targetHeight;
	
	// Controllers
	private SensorController sensorController;
	private CameraController cameraController;
	private MotorController motorController;
	
	// Flags
	
	private QRCodeHandler qrCodeReader;
	
	/**
	 * Meest recente string die de zeppelin heeft gedecodeerd uit een QR-code.
	 */
	private String mostRecentQRDecode;
	
	/**
	 * De zeppelin is nu bezig met landen en daarna de uitvoering te stoppen.
	 */
	private boolean exit = false;
	
	private HeightAdjuster heightAdjuster;
	
	private LogWriter logWriter = new LogWriter();

	public Zeppelin() throws RemoteException {
		super();
		sensorController = new SensorController(RaspiPin.GPIO_03, RaspiPin.GPIO_06);
		cameraController = new CameraController();
		motorController = new MotorController();
		qrCodeReader = new QRCodeHandler();
		heightAdjuster = new HeightAdjuster(motorController);
		
		logWriter.writeToLog("------------ START NIEUWE SESSIE ------------- \n");
		
		try {
			this.targetHeight = sensorController.sensorReading();
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
	
	@Override
	public ArrayList<Motor> getMotors() throws RemoteException {
		return this.motorController.getMotors();
	}
	
	@Override
	public Map<String, String> queryState() throws RemoteException {
		// TODO status van de motoren
		Map<String, String> status = new HashMap<String, String>();
		status.put("Hoogte",Double.toString(this.mostRecentHeight));
		return status;
	}
	
	public void startGameLoop() throws InterruptedException, RemoteException {
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
	 * @throws RemoteException 
	 */
	private void gameLoop() throws InterruptedException, RemoteException {
		while (!exit) {
			try {
				this.mostRecentHeight = sensorController.sensorReading();
				this.heightAdjuster.takeAction(mostRecentHeight, targetHeight);
			} catch (TimeoutException e) {
				e.printStackTrace();
			}
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		this.motorController.stopRightAndLeftMotor();
		this.motorController.stopHeightAdjustment();
		System.exit(0);
	}
	
	public String getMostRecentDecode() {
		return this.mostRecentQRDecode;
	}

	@Override
	public String readNewQRCode() throws RemoteException, IOException, InterruptedException {
		String filename = Long.toString(System.currentTimeMillis());
		this.cameraController.takePicture(filename);
		String decoded = this.qrCodeReader.read(FTPFileInfo.PATH_TO_FTP_FILES + filename + ".jpg");
		if (decoded != null) {
			this.mostRecentQRDecode = decoded;
			BufferedWriter output = new BufferedWriter(new FileWriter(
					FTPFileInfo.PATH_TO_FTP_FILES + FTPFileInfo.TIMESTAMPLIST_HOSTFILENAME, true));
			output.append("/n");
			output.append(filename + "," + decoded);
			output.close();
		}
		return decoded;
	}

	@Override
	public boolean leftIsOn() throws RemoteException {
		return this.motorController.leftIsOn();
	}

	@Override
	public boolean rightIsOn() throws RemoteException {
		return this.motorController.rightIsOn();
	}

	@Override
	public boolean downwardIsOn() throws RemoteException {
		return this.motorController.downwardIsOn();
	}

	@Override
	public void goForward() throws RemoteException {
		this.motorController.forward();
		
	}

	@Override
	public void goBackward() throws RemoteException {
		this.motorController.backward();
	}

	@Override
	public void turnLeft() throws RemoteException {
		this.motorController.left();
	}

	@Override
	public void turnRight() throws RemoteException {
		this.motorController.right();
	}

	@Override
	public void stopRightAndLeft() throws RemoteException {
		this.motorController.stopRightAndLeftMotor();
	}
	
	public void stopDownward() throws RemoteException {
		this.motorController.stopHeightAdjustment();
	}

}
