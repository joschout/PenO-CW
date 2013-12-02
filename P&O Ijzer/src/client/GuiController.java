package client;

import it.sauronsoftware.ftp4j.FTPAbortedException;
import it.sauronsoftware.ftp4j.FTPDataTransferException;
import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;

import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import server.ZeppelinServer;
import zeppelin.MainProgramInterface;


public class GuiController {


	/**
	 * Zeppelin-object gehaald van de server
	 */
	private MainProgramInterface zeppelin;
	
	private WebClient ftpClient;
	
	public GuiController() throws NotBoundException, IllegalStateException, IOException, FTPIllegalReplyException, FTPException {
		setZeppelin();
		setWebClient();
	}



	/**
	 * Associeer een zeppelin-object met deze GUI.
	 * @param zeppelin
	 * 		Een zeppelin-object geïmporteerd vanop de Pi.
	 */
	public void setZeppelin(MainProgramInterface zeppelin)
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
		MainProgramInterface zeppelin = (MainProgramInterface) registry.lookup("Zeppelin");
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
	
	public boolean qrCodeAvailable() throws RemoteException {
		return this.zeppelin.qrCodeAvailable();
	}
	
	public void consumeQRCode() throws RemoteException {
		this.zeppelin.qrCodeConsumed();
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
	
	public String[] getLastScannedQrCodeInfo() throws IllegalStateException, IOException, FTPIllegalReplyException, FTPException, FTPDataTransferException, FTPAbortedException {
		return this.ftpClient.getLastScannedQrCodeInfo();
	}
	
	public BufferedImage getImageFromFile(String filename) throws IllegalStateException, IOException, FTPIllegalReplyException, FTPException, FTPDataTransferException, FTPAbortedException {
		return this.ftpClient.getImageFromFile(filename);
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
	
	public void setKpHeight(double kp) {
		try {
			zeppelin.setKpHeight(kp);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	public void setKiHeight(double ki) {
		try {
			zeppelin.setKiHeight(ki);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	public void setKdHeight(double kd) {
		try {
			zeppelin.setKdHeight(kd);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	public double getKpHeight() throws RemoteException {
		return zeppelin.getKpHeight();
	}
	
	public double getKdHeight() throws RemoteException {
		return zeppelin.getKdHeight();
	}
	
	public double getKiHeight() throws RemoteException {
		return zeppelin.getKiHeight();
	}
	
	public void setSafetyIntervalHeight(double safetyInterval) throws RemoteException {
		zeppelin.setSafetyIntervalHeight(safetyInterval);
	}
	
	public double getSafetyIntervalHeight() throws RemoteException {
		return zeppelin.getSafetyIntervalHeight();
	}
	
	public void setKpAngle(double kp) {
		try {
			zeppelin.setKpAngle(kp);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	public void setKiAngle(double ki) {
		try {
			zeppelin.setKiAngle(ki);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	public void setKdAngle(double kd) {
		try {
			zeppelin.setKdAngle(kd);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	public double getKpAngle() throws RemoteException {
		return zeppelin.getKpAngle();
	}
	
	public double getKdAngle() throws RemoteException {
		return zeppelin.getKdAngle();
	}
	
	public double getKiAngle() throws RemoteException {
		return zeppelin.getKiAngle();
	}
	
	public void setSafetyIntervalAngle(double safetyInterval) throws RemoteException {
		zeppelin.setSafetyIntervalAngle(safetyInterval);
	}
	
	public double getSafetyIntervalAngle() throws RemoteException {
		return zeppelin.getSafetyIntervalAngle();
	}

}
