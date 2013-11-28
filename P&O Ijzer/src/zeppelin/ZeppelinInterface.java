/**
 * Deze interface dient als de brug tussen de cli�nt op ��n van onze laptops en de server op de Raspberry Pi.
 * De cli�nt roept geen methoden op op een object van klasse Zeppelin, maar op een object van deze klasse.
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
	
	
	
	public double sensorReading() throws RemoteException; // TODO: Gaat de cli�nt in de uiteindelijke versie ooit deze methode direct oproepen?
	
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
	
	public double getTargetHeight() throws RemoteException;
	
	public void setTargetHeight(double height) throws RemoteException;
	
	public ArrayList<Motor> getMotors() throws RemoteException;
	
	public boolean leftIsOn() throws RemoteException;
	
	public boolean rightIsOn() throws RemoteException;
	
	public boolean downwardIsOn() throws RemoteException;
	
	public void goForward() throws RemoteException;
	
	public void goBackward() throws RemoteException;
	
	public void turnLeft() throws RemoteException;
	
	public void turnRight() throws RemoteException;
	
	public void stopRightAndLeft() throws RemoteException;
	
	public void stopDownward() throws RemoteException;
	
	public void setKp(double kp) throws RemoteException;
	
	public void setKi(double ki) throws RemoteException;
	
	public void setKd(double kd) throws RemoteException;
	
	public String getMostRecentDecode() throws RemoteException;

}
