/**
 * Cliënt-klasse om sensoruitlezingen vanop de Pi te testen.
 * 
 * TODO: In de uiteindelijke versie verdwijnt deze klasse best: integreer de functionaliteit in de GUI.
 */

package gui;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import zeppelin.ZeppelinInterface;

public class TestClient {
	
	public static double sensorReading() {
		try {
			Registry registry = LocateRegistry.getRegistry("192.168.2.100",1099); // probeer verbinding te maken met het RMI-register
			                                                                      // dat normaal gezien bestaat op de Pi.
			ZeppelinInterface zeppelin = null;
			try {
				zeppelin = (ZeppelinInterface) registry.lookup ("Zeppelin"); // zoek naar het geëxporteerde Zeppelin-object:
				                                                             // dit is van de klasse ZeppelinInterface omdat
				                                                             // in theorie de cliënt geen toegang heeft tot de code
				                                                             // van de klasse Zeppelin.
			} catch (NotBoundException e) {
				e.printStackTrace();
			}
			return zeppelin.sensorReading();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return 0;
	}

}
