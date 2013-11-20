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

import server.ZeppelinServer;
import zeppelin.MainProgramInterface;
import QRCode.QRCodeHandler;

import com.google.zxing.WriterException;

import components.Motor;


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
	 * 		Een zeppelin-object ge�mporteerd vanop de Pi.
	 */
	public void setZeppelin(MainProgramInterface zeppelin)
	{
		this.zeppelin = zeppelin;
	}

	public double getHeight() throws RemoteException {
		return this.zeppelin.sensorReading();
	}


	public void setZeppelin() throws RemoteException, NotBoundException {
		Registry registry = LocateRegistry.getRegistry(ZeppelinServer.PI_HOSTNAME,1099);
		MainProgramInterface zeppelin = (MainProgramInterface) registry.lookup("Zeppelin");
		this.zeppelin = zeppelin;
	}
	
	public void setWebClient() throws IllegalStateException, IOException, FTPIllegalReplyException, FTPException {
		this.ftpClient = new WebClient();
	}
	
	public boolean leftIsOn() throws RemoteException {
		return this.zeppelin.leftIsOn();
	}
	
	public boolean rightIsOn() throws RemoteException {
		return this.zeppelin.rightIsOn();
	}
	
	public boolean downwardIsOn() throws RemoteException {
		return this.zeppelin.downwardIsOn();
	}
	
	public boolean qrCodeAvailable() throws RemoteException {
		return this.zeppelin.qrCodeAvailable();
	}
	
	public void consumeQRCode() throws RemoteException {
		this.zeppelin.qrCodeConsumed();
	}
	
	public void goForward() throws RemoteException {
		this.zeppelin.goForward();
	}
	
	public void goBackward() throws RemoteException {
		this.zeppelin.goBackward();
	}
	
	public void goLeft() throws RemoteException {
		this.zeppelin.turnLeft();
	}
	
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

}
