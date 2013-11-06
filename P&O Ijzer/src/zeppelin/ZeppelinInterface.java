/**
 * Deze interface dient als de brug tussen de cliënt op één van onze laptops en de server op de Raspberry Pi.
 * De cliënt roept geen methoden op op een object van klasse Zeppelin, maar op een object van deze klasse.
 */

package zeppelin;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

import javax.swing.ImageIcon;

public interface ZeppelinInterface extends Remote {
	
	public static final String TIMESTAMPLIST_HOSTFILENAME = "timestamplist";
	public static final String TIMESTAMPLIST_LOCALFILENAME = "timestamplist.txt";
	public static final String PATH_TO_FTP_FILES = "/home/pi/ftp/files/";
	
	/**
	 * Activeert de naar-beneden-gerichte motor.
	 * @throws RemoteException
	 */
	public void activateDownwardMotor() throws RemoteException;
	
	/**
	 * Activeert de naar-links-gerichte motor.
	 * @throws RemoteException
	 */
	public void activateLeftMotor() throws RemoteException;
	
	/**
	 * Activeert de naar-rechts-gerichte motor.
	 * @throws RemoteException
	 */
	public void activateRightMotor() throws RemoteException;
	
	/**
	 * Activeert de naar-achter-gerichte motor.
	 * @throws RemoteException
	 */
	public void activateBackwardMotor() throws RemoteException; // TODO: Hebben we zelfs zulk een motor beschikbaar?
	
	/**
	 * Leest de afstand tot de grond uit van de sensor.
	 * @return De afstand tussen de sensor en de grond.
	 * @throws RemoteException
	 */
	public double sensorReading() throws RemoteException; // TODO: Gaat de cliënt in de uiteindelijke versie ooit deze methode direct oproepen?
	
	/**
	 * Zorg ervoor dat de zeppelin stopt in een veilige toestand.
	 * @throws RemoteException
	 */
	public void exit() throws RemoteException;
	
	/**
	 * Geeft de toestand van de vier motoren en de meest recente lezing van de sensor
	 * @return Een map die key-value paren bevat van de vorm: "DownwardMotor: INACTIVE"
	 * @throws RemoteException
	 */
	public Map<String,String> queryState() throws RemoteException;
	
	/**
	 * De zeppelin neemt een foto en probeert een QR-code te lezen uit die foto.
	 * @return	Een string als er een QR-code kon gedecodeerd worden, anders null.
	 * @throws RemoteException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public String readNewQRCode() throws RemoteException, IOException, InterruptedException;
	
	public String getMostRecentDecode() throws RemoteException;

}
