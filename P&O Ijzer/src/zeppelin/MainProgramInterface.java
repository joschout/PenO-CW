
/**
 * Deze interface dient als de brug tussen de cliënt op één van onze laptops en de server op de Raspberry Pi.
 * De cliënt roept geen methoden op op een object van klasse Zeppelin, maar op een object van deze klasse.
 */

package zeppelin;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

import movement.HeightController;
import movement.RotationController;

public interface MainProgramInterface extends Remote {
	
	
	/**
	 * Geeft de meest recent gemeten hoogte terug.
	 * @return	De meest recent gemeten hoogte.
	 * @throws	RemoteException
	 */
	//public double sensorReading() throws RemoteException;
	
	/**
	 * Zorg ervoor dat de zeppelin stopt in een veilige toestand.
	 * @throws RemoteException
	 */
	public void exit() throws RemoteException;
	
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
	
	public double getHeight() throws RemoteException;

	public HeightController getHeightController() throws RemoteException;

	public RotationController getRotationController() throws RemoteException;
	
	
	
	//TODO OBSOLETE
//	
//	/**
//	 * Update de procesconstante voor de hoogte.
//	 * @throws RemoteException
//	 */
//	public void setKpHeight(double kp) throws RemoteException;
//	
//	/**
//	 * Update de integraalconstante voor de hoogte.
//	 * @throws RemoteException
//	 */
//	public void setKiHeight(double ki) throws RemoteException;
//	
//	/**
//	 * Update de derivative constante voor de hoogte.
//	 * @throws RemoteException
//	 */
//	public void setKdHeight(double kd) throws RemoteException;
//	
//	/**
//	 * Haal de procesconstante voor de hoogte.
//	 * @throws RemoteException
//	 */
//	public double getKpHeight() throws RemoteException;
//	
//	/**
//	 * Haal de integraalconstante voor de hoogte.
//	 * @throws RemoteException
//	 */
//	public double getKiHeight() throws RemoteException;
//	
//	/**
//	 * Haal de derivative constante voor de hoogte.
//	 * @throws RemoteException
//	 */
//	public double getKdHeight() throws RemoteException;
//	
//	/**
//	 * Update het veiligheidsinterval rond de hoogte. Als de gemeten hoogte van de zeppelin
//	 * in dit interval ligt, mag de verticale motor niets meer doen.
//	 * @param safetyInterval
//	 *        Maakt een interval volgens: [getTargetHeight - safetyInterval, getTargetHeight + safetyInterval]
//	 * @throws RemoteException
//	 */
//	public void setSafetyIntervalHeight(double safetyInterval) throws RemoteException;
//	
//	/**
//	 * Haal het veiligheidsinterval rond de hoogte.
//	 * @throws RemoteException
//	 */
//	public double getSafetyIntervalHeight() throws RemoteException;
//	
//	/**
//	 * Update de procesconstante voor de hoek.
//	 * @throws RemoteException
//	 */
//	public void setKpAngle(double kp) throws RemoteException;
//	
//	/**
//	 * Update de integraalconstante voor de hoek.
//	 * @throws RemoteException
//	 */
//	public void setKiAngle(double ki) throws RemoteException;
//	
//	/**
//	 * Update de derivative constante voor de hoek.
//	 * @throws RemoteException
//	 */
//	public void setKdAngle(double kd) throws RemoteException;
//	
//	/**
//	 * Haal de procesconstante voor de hoek.
//	 * @throws RemoteException
//	 */
//	public double getKpAngle() throws RemoteException;
//	
//	/**
//	 * Haal de integraalconstante voor de hoek.
//	 * @throws RemoteException
//	 */
//	public double getKiAngle() throws RemoteException;
//	
//	/**
//	 * Haal de derivative constante voor de hoek.
//	 * @throws RemoteException
//	 */
//	public double getKdAngle() throws RemoteException;
//	
//	/**
//	 * Update het veiligheidsinterval rond de doelhoek. Als de gemeten hoek van de zeppelin
//	 * in dit interval ligt, mogen de horizontale motoren niets meer doen.
//	 * @param safetyInterval
//	 *        Maakt een interval als volgt: [getTargetAngle - safetyInterval, getTargetAngle + safetyInterval]
//	 * @throws RemoteException
//	 */
//	public void setSafetyIntervalAngle(double safetyInterval) throws RemoteException;
//	
//	/**
//	 * Haal het veiligheidsinterval rond de hoek.
//	 * @throws RemoteException
//	 */
//	public double getSafetyIntervalAngle() throws RemoteException;
//	*/
	
	/**
	 * Leest de log.
	 * @throws RemoteException
	 */
	public String readLog() throws RemoteException;


}
