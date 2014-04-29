/**
 * Communicatielaag tussen de client en de zeppelin. Status opvragen van de zeppelin en relevante
 * files downloaden van de zeppelin gebeurt via deze klasse.
 */

package client;

import it.sauronsoftware.ftp4j.FTPAbortedException;
import it.sauronsoftware.ftp4j.FTPDataTransferException;
import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;

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
	
	public GuiController() throws NotBoundException, IllegalStateException, IOException, FTPIllegalReplyException, FTPException {
		setZeppelin();
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
	 * Vraagt de hoogte van de zeppelin op.
	 * @return
	 * @throws RemoteException
	 */
	public double getHeight() throws RemoteException {
		return this.zeppelin.getHeight();
	}


	/**
	 * Zoekt een zeppelin-object op het IP-adres gereserveerd voor de zeppelin.
	 * Communicatie gebeurt daarna tussen de client en het gevonden zeppelin-object.
	 * @throws RemoteException
	 * @throws NotBoundException
	 * 		   Er bestaat geen zeppelin-object.
	 */
	public void setZeppelin() throws RemoteException, NotBoundException {
		Registry registry = LocateRegistry.getRegistry(ZeppelinServer.PI_HOSTNAME,1099);
		MainProgramInterface zeppelin = (MainProgramInterface) registry.lookup("Zeppelin");
		this.zeppelin = zeppelin;
	}
	
	/**
	 * Geeft aan of de linkermotor aanstaat.
	 * @throws RemoteException
	 */
	public boolean leftIsOn() throws RemoteException {
		return this.zeppelin.leftIsOn();
	}
	
	/**
	 * Geeft aan of de rechtermotor aanstaat.
	 * @throws RemoteException
	 */
	public boolean rightIsOn() throws RemoteException {
		return this.zeppelin.rightIsOn();
	}
	
	/**
	 * Geeft aan of de naar-beneden-gerichte motor aanstaat.
	 * @return
	 * @throws RemoteException
	 */
	public boolean downwardIsOn() throws RemoteException {
		return this.zeppelin.downwardIsOn();
	}
	
	/**
	 * Laat de zeppelin vooruit gaan.
	 * @throws RemoteException
	 */
	public void goForward() throws RemoteException {
		this.zeppelin.goForward();
	}
	
	/**
	 * Laat de zeppelin achteruit gaan.
	 * @throws RemoteException
	 */
	public void goBackward() throws RemoteException {
		this.zeppelin.goBackward();
	}
	
	/**
	 * Laat de zeppelin naar links draaien.
	 * @throws RemoteException
	 */
	public void goLeft() throws RemoteException {
		this.zeppelin.turnLeft();
	}
	
	/**
	 * Laat de zeppelin naar rechts draaien.
	 * @throws RemoteException
	 */
	public void goRight() throws RemoteException {
		this.zeppelin.turnRight();
	}
	
	/**
	 * Laat de zeppelin de horizontaal gerichte motoren stoppen.
	 * @throws RemoteException
	 */
	public void stopRightAndLeftMotor() throws RemoteException {
		this.zeppelin.stopRightAndLeft();
	}
	
	/**
	 * Haal de doelhoogte van de zeppelin.
	 * @throws RemoteException
	 */
	public double getTargetHeight() throws RemoteException {
		return this.zeppelin.getTargetHeight();
	}
	
	/**
	 * Laat de zeppelin streven naar een nieuwe hoogte.
	 * @param height
	 *        Nieuwe hoogte.
	 * @throws RemoteException
	 */
	public void setTargetHeight(double height) throws RemoteException {
		this.zeppelin.setTargetHeight(height);
	}
	
	/**
	 * Leest de log op de zeppelin.
	 * @throws IllegalStateException
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws FTPIllegalReplyException
	 * @throws FTPException
	 * @throws FTPDataTransferException
	 * @throws FTPAbortedException
	 */
	public String readLog() throws IllegalStateException, FileNotFoundException, IOException, FTPIllegalReplyException, FTPException, FTPDataTransferException, FTPAbortedException {
		return this.zeppelin.readLog();
	}

	/**
	 * Laat de zeppelin zijn activiteiten stoppen; sluit achteraf het programma af.
	 */
	public void exit() {
		try {
			this.zeppelin.exit();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		System.exit(0);
	}
	
	/**
	 * Update de procesconstante voor de hoogte.
	 * @param kp
	 * @throws RemoteException 
	 */
	public void setKpHeight(double kp) throws RemoteException {
		zeppelin.getHeightController().getpController().setKp(kp);
	}
	
	/**
	 * Update de integraalconstante voor de hoogte.
	 * @param ki
	 * @throws RemoteException 
	 */
	public void setKiHeight(double ki) throws RemoteException {
		zeppelin.getHeightController().getpController().setKi(ki);
	}
	
	/**
	 * Update de derivative constante voor de hoogte.
	 * @param kd
	 * @throws RemoteException 
	 */
	public void setKdHeight(double kd) throws RemoteException {
		zeppelin.getHeightController().getpController().setKd(kd);
	}
	
	/**
	 * Haalt de procesconstante voor de hoogte.
	 * @throws RemoteException
	 */
	public double getKpHeight() throws RemoteException {
		return zeppelin.getHeightController().getpController().getKp();
	}
	
	/**
	 * Haalt de derivate constante voor de hoogte.
	 * @throws RemoteException
	 */
	public double getKdHeight() throws RemoteException {
		return zeppelin.getHeightController().getpController().getKd();
	}
	
	/**
	 * Haalt de integraalconstante voor de hoogte.
	 * @throws RemoteException
	 */
	public double getKiHeight() throws RemoteException {
		return zeppelin.getHeightController().getpController().getKi();
	}
	
	// wordt deze methode zelfs gebruikt?
	/**
	 * Zet het interval rond de doelhoogte waar de verticale motor alle activiteit
	 * moet stoppen.
	 * @param safetyInterval
	 *        Waarde die een interval maakt volgens [getTargetHeight - safetyInterval, getTargetHeight + safetyInterval]
	 * @throws RemoteException
	 */
	public void setSafetyIntervalHeight(double safetyInterval) throws RemoteException {
		zeppelin.getHeightController().setSafetyIntervalHeight(safetyInterval);
	}
	
	/**
	 * Haalt het veiligheidsinterval voor de hoogte.
	 * @throws RemoteException
	 */
	public double getSafetyIntervalHeight() throws RemoteException {
		return zeppelin.getHeightController().getSafetyIntervalHeight();
	}
	
	/**
	 * Update de procesconstante voor de hoek.
	 * @param kp
	 * @throws RemoteException 
	 */
	public void setKpAngle(double kp) throws RemoteException {
		zeppelin.getRotationController().getpController().setKp(kp);
	}
	
	/**
	 * Update de integraalconstante voor de hoek.
	 * @param ki
	 * @throws RemoteException 
	 */
	public void setKiAngle(double ki) throws RemoteException {
		zeppelin.getRotationController().getpController().setKi(ki);
	}
	
	/**
	 * Update de derivative constante voor de hoek.
	 * @param kd
	 * @throws RemoteException 
	 */
	public void setKdAngle(double kd) throws RemoteException {
		zeppelin.getRotationController().getpController().setKd(kd);
	}
	
	/**
	 * Haal de procesconstante voor de hoek.
	 * @throws RemoteException
	 */
	public double getKpAngle() throws RemoteException {
		return zeppelin.getRotationController().getpController().getKp();
	}
	
	/**
	 * Haal de derivative constante voor de hoek.
	 * @throws RemoteException
	 */
	public double getKdAngle() throws RemoteException {
		return zeppelin.getRotationController().getpController().getKd();
	}
	
	/**
	 * Haal de integraalconstante voor de hoek.
	 * @throws RemoteException
	 */
	public double getKiAngle() throws RemoteException {
		return zeppelin.getRotationController().getpController().getKi();
	}
	
	/**
	 * Zet het interval rond de doelhoek waar de zeppelin de horizontale motoren alle
	 * activiteit moet laten stoppen.
	 * @param safetyInterval
	 * 		  Maakt een interval rond de doelhoek volgens [getTargetAngle - safetyInterval, getTargetAngle + safetyInterval].
	 * 		  Natuurlijk gerekend modulo 360.
	 * @throws RemoteException
	 */
	public void setSafetyIntervalAngle(double safetyInterval) throws RemoteException {
		zeppelin.getRotationController().setSafetyIntervalAngle(safetyInterval);
	}
	
	/**
	 * Haal het interval waar de zeppelin de horizontale motoren niet meer mag laten werken.
	 * @throws RemoteException
	 */
	public double getSafetyIntervalAngle() throws RemoteException {
		return zeppelin.getRotationController().getSafetyIntervalAngle();
	}

}

