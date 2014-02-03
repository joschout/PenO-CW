/**
 * Interface om de client de oriëntatiehoek van de zeppelin te laten afleiden
 * uit een foto van een QR-code, wegens te rekenintensief voor de Raspberry Pi
 * om snel genoeg resultaat te krijgen voor onze doeleinden.
 */

package client;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface FTPOrientationIface extends Remote {
	
	public double findOrientationFromPicture(String filename) throws RemoteException;
}
