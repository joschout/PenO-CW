package client;

import java.awt.HeadlessException;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import server.ZeppelinServer;
import zeppelin.ZeppelinInterface;
import QRCode.QRCodeOperations;

import com.google.zxing.WriterException;


public class GuiController {


	// Zeppelin-object gehaald van de server
	private ZeppelinInterface zeppelin;

	public GuiController() throws RemoteException, NotBoundException {
		setZeppelin();
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

	public double sensorReading() throws RemoteException {
		return this.zeppelin.sensorReading();
	}


	public void setZeppelin() throws RemoteException, NotBoundException {
		Registry registry = LocateRegistry.getRegistry(ZeppelinServer.host,1099);
		ZeppelinInterface zeppelin = (ZeppelinInterface) registry.lookup("Zeppelin");
		this.zeppelin = zeppelin;
	}

	public ImageIcon scanQRCode(String filename) throws InterruptedException, IOException{
		return this.zeppelin.takeNewImage(filename);
	}
	
	public String newQRReading() {
		try {
			this.zeppelin.readNewQRCode();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		try {
			return this.zeppelin.getMostRecentDecode();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void exit() {
		System.exit(0);
	}

}
