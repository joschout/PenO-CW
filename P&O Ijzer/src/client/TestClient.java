/**
 * Cliënt-klasse om sensoruitlezingen vanop de Pi te testen.
 * 
 * TODO: In de uiteindelijke versie verdwijnt deze klasse best: integreer de functionaliteit in de GUI.
 */

package client;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import javax.swing.ImageIcon;

import zeppelin.ZeppelinInterface;

public class TestClient {
	
	public static void main(String[] args) throws InterruptedException, IOException {
		try {
			Registry registry = LocateRegistry.getRegistry("192.168.2.150",1099); // probeer verbinding te maken met het RMI-register
			                                                                      // dat normaal gezien bestaat op de Pi.
			ZeppelinInterface zeppelin = null;
			try {
				zeppelin = (ZeppelinInterface) registry.lookup ("Zeppelin"); // zoek naar het geëxporteerde Zeppelin-object:
				                                                             // dit is van de klasse ZeppelinInterface omdat
				                                                             // in theorie de cliënt geen toegang heeft tot de code
				                                                             // van de klasse Zeppelin.
				zeppelin.takeNewImage(Long.toString(System.currentTimeMillis()));
				System.exit(0);
			} catch (NotBoundException e) {
				e.printStackTrace();
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

}
