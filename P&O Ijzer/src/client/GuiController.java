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

	public double getHeight() throws RemoteException {
		return this.zeppelin.sensorReading();
	}


	public void setZeppelin() throws RemoteException, NotBoundException {
		Registry registry = LocateRegistry.getRegistry(ZeppelinServer.PI_HOSTNAME,1099);
		ZeppelinInterface zeppelin = (ZeppelinInterface) registry.lookup("Zeppelin");
		this.zeppelin = zeppelin;
	}
	
	public void setWebClient() throws IllegalStateException, IOException, FTPIllegalReplyException, FTPException {
		this.ftpClient = new WebClient();
	}
	
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
		System.exit(0);
	}

}
