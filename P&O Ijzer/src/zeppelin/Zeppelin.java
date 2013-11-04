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
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;

import com.pi4j.io.gpio.RaspiPin;

import controllers.CameraController;
import controllers.SensorController;
import controllers.SensorController.TimeoutException;

public class Zeppelin extends UnicastRemoteObject implements ZeppelinInterface {

	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Meest recente uitlezing van de sensor.
	 */
	private double height;
	
	// Controllers
	private SensorController sensorController;
	private CameraController cameraController;
	
	private static final String timeStampListFileName = "timestamplist";
	
	// Flags
	/**
	 * Cli�nt heeft bepaald dat uitvoering moet stoppen.
	 */
	private boolean exit = false;
	
	/**
	 * Meest recente string die de zeppelin heeft gedecodeerd uit een QR-code.
	 */
	private String mostRecentQRDecode;

	public Zeppelin() throws RemoteException {
		super();
		sensorController = new SensorController(RaspiPin.GPIO_00, RaspiPin.GPIO_07);
		cameraController = new CameraController();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void activateDownwardMotor() throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void activateLeftMotor() throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void activateRightMotor() throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void activateBackwardMotor() throws RemoteException {
		// TODO Auto-generated method stub

	}
	
	@Override
	public double sensorReading() throws RemoteException {
		// TODO Auto-generated method stub
		return this.height;
	}
	
	

	@Override
	public Map<String, String> queryState() throws RemoteException {
		// TODO status van de motoren
		Map<String, String> status = new HashMap<String, String>();
		status.put("Hoogte",Double.toString(this.height));
		return status;
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
	 * Zolang de cli�nt contact onderhoudt met de zeppelin, moet deze
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
				this.height = sensorController.sensorReading();
			} catch (TimeoutException e) {
				e.printStackTrace();
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public ImageIcon takeNewImage(String filename) throws InterruptedException, IOException {
		// TODO Auto-generated method stub
		cameraController.imageToObject(filename);
		return cameraController.getImage();
	}
	
	public String getMostRecentDecode() {
		return this.mostRecentQRDecode;
	}

	@Override
	public void readNewQRCode() throws RemoteException, IOException, InterruptedException {
		String filename = Long.toString(System.currentTimeMillis());
		this.cameraController.takePicture(filename);
		this.mostRecentQRDecode = QRCode.QRCodeOperations.read(filename);
		BufferedWriter output = new BufferedWriter(new FileWriter(timeStampListFileName, true));
		output.append("/n");
		output.append(filename);
		output.close();
		// System.out.println("Meest recente gedecodeerde string op zeppelin: " + this.mostRecentQRDecode);
	}

}
