/**
 * Deze interface dient als de brug tussen de cliënt op één van onze laptops en de server op de Raspberry Pi.
 * De cliënt roept geen methoden op op een object van klasse Zeppelin, maar op een object van deze klasse.
 */

package zeppelin;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;

import components.Motor;

public interface MainProgramInterface extends Remote {
	
	
	/**
	 * Geeft de meest recent gemeten hoogte terug.
	 * @return	De meest recent gemeten hoogte.
	 * @throws	RemoteException
	 */
	public double sensorReading() throws RemoteException;
	
	/**
	 * Zorg ervoor dat de zeppelin stopt in een veilige toestand.
	 * @throws RemoteException
	 */
	public void exit() throws RemoteException;
	
	/**
	 * De zeppelin neemt een foto en probeert een QR-code te lezen uit die foto.
	 * @return	Een string als er een QR-code kon gedecodeerd worden, anders null.
	 * @throws RemoteException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public String readNewQRCode() throws RemoteException, IOException, InterruptedException;
	
	/**
	 * Geeft de doelhoogte terug.
	 * @return	De doelhoogte.
	 * @throws RemoteException
	 */
	public double getTargetHeight() throws RemoteException;
	
	/**
	 * Bepaalt de doelhoogte van de zeppelin.
	 * @param	height
	 * 			De hoogte waar de zeppelin op moet zweven.
	 * @throws RemoteException
	 */
	public void setTargetHeight(double height) throws RemoteException;
	
	/**
	 * Geeft aan of de linkermotor aanstaat.
	 * @return	De linkermotor staat aan.
	 * @throws RemoteException
	 */
	public boolean leftIsOn() throws RemoteException;
	
	/**
	 * Geeft aan of de rechtermotor aanstaat.
	 * @return	De rechtermotor staat aan.
	 * @throws RemoteException
	 */
	public boolean rightIsOn() throws RemoteException;
	
	/**
	 * Geeft aan of de naar-onder-gerichte motor aanstaat.
	 * @return	De naar-onder-gerichte motor staat aan.
	 * @throws RemoteException
	 */
	public boolean downwardIsOn() throws RemoteException;
	
	/**
	 * Laat de zeppelin vooruit gaan.
	 * @throws RemoteException
	 */
	public void goForward() throws RemoteException;
	
	/**
	 * Laat de zeppelin achteruit gaan.
	 * @throws RemoteException
	 */
	public void goBackward() throws RemoteException;
	
	/**
	 * Laat de zeppelin naar links draaien.
	 * @throws RemoteException
	 */
	public void turnLeft() throws RemoteException;
	
	/**
	 * Laat de zeppelin naar rechts draaien.
	 * @throws RemoteException
	 */
	public void turnRight() throws RemoteException;
	
	/**
	 * Laat de zeppelin de linker- en rechtermotor afzetten, mochten die aanstaan.
	 * @throws RemoteException
	 */
	public void stopRightAndLeft() throws RemoteException;
	
	/**
	 * Laat de zeppelin de naar-onder-gerichte motor afzetten, mocht die aanstaan.
	 * @throws RemoteException
	 */
	public void stopDownward() throws RemoteException;
	
	/**
	 * Geeft aan of de zeppelin minstens één nieuwe QR-code heeft gescand.
	 * @return	De zeppelin heeft mintens één nieuwe QR-code beschikbaar.
	 * @throws RemoteException
	 */
	public boolean qrCodeAvailable() throws RemoteException;
	
	/**
	 * Laat weten aan de zeppelin dat de meest recent gescande QR-code afgehaald is
	 * vanop de FTP-server.
	 * @throws RemoteException
	 */
	public void qrCodeConsumed() throws RemoteException;public void setKp(double kp) throws RemoteException;
	
	public void setKi(double ki) throws RemoteException;
	
	public void setKd(double kd) throws RemoteException;
	
	public double getKp() throws RemoteException;
	
	public double getKi() throws RemoteException;
	
	public double getKd() throws RemoteException;
	
	public void setSafetyInterval(double safetyInterval) throws RemoteException;
	
	public double getSafetyInterval() throws RemoteException;

}
