package client;

import it.sauronsoftware.ftp4j.FTPAbortedException;
import it.sauronsoftware.ftp4j.FTPDataTransferException;
import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;

import java.awt.HeadlessException;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import movement.PIDController;
import server.ZeppelinServer;
import zeppelin.ZeppelinInterface;
import QRCode.QRCodeHandler;

import com.google.zxing.WriterException;

import components.Motor;


public class GuiController {


	/**
	 * Zeppelin-object gehaald van de server
	 */
	private ZeppelinInterface zeppelin;
	
	private WebClient ftpClient;
	
	private PIDController pController = new PIDController();
	
	public GuiController() throws NotBoundException, IllegalStateException, IOException, FTPIllegalReplyException, FTPException {
		setZeppelin();
		setWebClient();
	}



	/**
	 * Associeer een zeppelin-object met deze GUI.
	 * @param zeppelin
	 * 		Een zeppelin-object geïmporteerd vanop de Pi.
	 */
	public void setZeppelin(ZeppelinInterface zeppelin)
	{
		this.zeppelin = zeppelin;
	}

	/**
	 * 
	 * @return
	 * @throws RemoteException
	 */
	public double getHeight() throws RemoteException {
		return this.zeppelin.sensorReading();
	}


	/**
	 * 
	 * @throws RemoteException
	 * @throws NotBoundException
	 */
	public void setZeppelin() throws RemoteException, NotBoundException {
		Registry registry = LocateRegistry.getRegistry(ZeppelinServer.PI_HOSTNAME,1099);
		ZeppelinInterface zeppelin = (ZeppelinInterface) registry.lookup("Zeppelin");
		this.zeppelin = zeppelin;
	}
	
	/**
	 * 
	 * @throws IllegalStateException
	 * @throws IOException
	 * @throws FTPIllegalReplyException
	 * @throws FTPException
	 */
	public void setWebClient() throws IllegalStateException, IOException, FTPIllegalReplyException, FTPException {
		this.ftpClient = new WebClient();
	}
	
	/**
	 * 
	 * @return
	 * @throws RemoteException
	 */
	public ArrayList<Boolean> getActiveMotors() throws RemoteException {
		ArrayList<Motor> motors = this.zeppelin.getMotors();
		ArrayList<Boolean> toReturn = new ArrayList<Boolean>();
		for (int i = 0; i < motors.size(); i++) {
			if (motors.get(i).isOn()) {
				toReturn.add(true);
			}
			else toReturn.add(false);
		}
		return toReturn;
	}
	
	/**
	 * 
	 * @return
	 * @throws RemoteException
	 */
	public boolean leftIsOn() throws RemoteException {
		return this.zeppelin.leftIsOn();
	}
	
	/**
	 * 
	 * @return
	 * @throws RemoteException
	 */
	public boolean rightIsOn() throws RemoteException {
		return this.zeppelin.rightIsOn();
	}
	
	/**
	 * 
	 * @return
	 * @throws RemoteException
	 */
	public boolean downwardIsOn() throws RemoteException {
		return this.zeppelin.downwardIsOn();
	}
	
	/**
	 * 
	 * @throws RemoteException
	 */
	public void goForward() throws RemoteException {
		this.zeppelin.goForward();
	}
	
	/**
	 * 
	 * @throws RemoteException
	 */
	public void goBackward() throws RemoteException {
		this.zeppelin.goBackward();
	}
	
	/**
	 * 
	 * @throws RemoteException
	 */
	public void goLeft() throws RemoteException {
		this.zeppelin.turnLeft();
	}
	
	/**
	 * 
	 * @throws RemoteException
	 */
	public void goRight() throws RemoteException {
		this.zeppelin.turnRight();
	}
	
	public void stopRightAndLeftMotor() throws RemoteException {
		this.zeppelin.stopRightAndLeft();
	}
	
	public double getTargetHeight() throws RemoteException {
		return this.zeppelin.getTargetHeight();
	}
	
	public void setTargetHeight(double height) throws RemoteException {
		this.zeppelin.setTargetHeight(height);
	}
	
	/**
	 * Laat de zeppelin een QR-code lezen.
	 * @return Het resultaat van de volgende oproep: zeppelin.readNewQRCode()
	 * @throws InterruptedException 
	 * @throws IOException 
	 * @throws RemoteException 
	 */
	public String newQRReading() throws RemoteException, IOException, InterruptedException {
		return this.zeppelin.readNewQRCode();
	}
	
	public BufferedImage getLastScannedImage() throws IllegalStateException, IOException, FTPIllegalReplyException, FTPException, FTPDataTransferException, FTPAbortedException {
		return this.ftpClient.getLastScannedImage();
	}
	
	public String readLogFile() throws IllegalStateException, FileNotFoundException, IOException, FTPIllegalReplyException, FTPException, FTPDataTransferException, FTPAbortedException {
		return this.ftpClient.readLogFile();
	}

	public void exit() {
		try {
			this.zeppelin.exit();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		System.exit(0);
	}
	
	public void setKp(double kp) {
		try {
			zeppelin.setKp(kp);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	public void setKi(double ki) {
		try {
			zeppelin.setKi(ki);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	public void setKd(double kd) {
		try {
			zeppelin.setKd(kd);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	public double getKp() throws RemoteException {
		return zeppelin.getKp();
	}
	
	public double getKd() throws RemoteException {
		return zeppelin.getKd();
	}
	
	public double getKi() throws RemoteException {
		return zeppelin.getKi();
	}
	
	public void setSafetyInterval(double safetyInterval) throws RemoteException {
		zeppelin.setSafetyInterval(safetyInterval);
	}
	
	public double getSafetyInterval() throws RemoteException {
		return zeppelin.getSafetyInterval();
	}

}
