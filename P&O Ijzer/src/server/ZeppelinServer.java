/**
 * Deze klasse maakt een Zeppelin object aan en maakt deze beschikbaar in het RMI-register.
 */

package server;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import controllers.SensorController.TimeoutException;

import zeppelin.Zeppelin;

public class ZeppelinServer {
	
	public static final String PI_HOSTNAME = "192.168.2.100";
	
	public static void main(String[] args) throws InterruptedException, TimeoutException {
		
		try {
		System.setProperty("java.rmi.server.hostname", PI_HOSTNAME); /* Maak kenbaar dat het RMI-register op dit adres
			 																  gevonden kan worden.
			 																  TODO: een manier vinden om te garanderen dat
			 																  de cliënt zich kan verbinden met het register,
			 																  hetzij door een statisch IP-adres voor de Pi te
			 																  verkrijgen, hetzij door, wanneer je de server start
			 																  op de Pi, het IP-adres van de Pi moet invoeren. */
		LocateRegistry.createRegistry(1099); // maak een register op de opgegeven poort.
		Zeppelin zeppelin = new Zeppelin();
		Naming.rebind("rmi://localhost:1099/Zeppelin", zeppelin); // maak de zeppelin beschikbaar in het register.
		zeppelin.startGameLoop(); // start d
		}
		catch (RemoteException e) {
			System.err.println("Fout bij het exporteren van het zeppelin-object.");
			e.printStackTrace();
		}
		catch (MalformedURLException e) {
			System.err.println("URL voor het exporteren van het zeppelin-object is fout.");
			e.printStackTrace();
		}
	}

}
