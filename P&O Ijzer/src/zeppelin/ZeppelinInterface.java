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

public interface ZeppelinInterface extends Remote {
	
	
	
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
	
	public void setTargetHeight(double height) throws RemoteException;
	
	public ArrayList<Motor> getMotors() throws RemoteException;
	
	public boolean leftIsOn() throws RemoteException;
	
	public boolean rightIsOn() throws RemoteException;
	
	public boolean downwardIsOn() throws RemoteException;
	
	public String getMostRecentDecode() throws RemoteException;

}
