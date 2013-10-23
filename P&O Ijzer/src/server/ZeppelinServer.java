/**
 * Deze klasse maakt een Zeppelin object aan en maakt deze beschikbaar in het RMI-register.
 */

package server;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import zeppelin.Zeppelin;

public class ZeppelinServer {
	
	/**
	 * Object dat de zeppelin representeert en wordt doorgegeven via het RMI-register.
	 */
	public Zeppelin zeppelin;
	
	public static void main(String[] args) {
		try {
			System.setProperty("java.rmi.server.hostname", "192.168.2.100"); /* Maak kenbaar dat het RMI-register op dit adres
			 																  gevonden kan worden.
			 																  TODO: een manier vinden om te garanderen dat
			 																  de cliënt zich kan verbinden met het register,
			 																  hetzij door een statisch IP-adres voor de Pi te
			 																  verkrijgen, hetzij door, wanneer je de server start
			 																  op de Pi, het IP-adres van de Pi moet invoeren. */
	    	LocateRegistry.createRegistry(1099); // maak een register op de opgegeven poort.
	    	try {
				Naming.rebind("rmi://localhost:1099/Zeppelin", new Zeppelin()); // maak de zeppelin beschikbaar in het register.
				System.out.println("Server is ready.");
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		} catch (RemoteException e1) {
			e1.printStackTrace();
		}
	}

}
