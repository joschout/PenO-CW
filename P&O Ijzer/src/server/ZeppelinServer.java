/**
 * Deze klasse maakt een Zeppelin object aan en maakt deze beschikbaar in het RMI-register.
 */

package server;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import controllers.SensorController.TimeoutException;

import zeppelin.MainProgramImpl;

public class ZeppelinServer {
	
	//Het IP-adres van de Raspberry Pi op het ad-hoc netwerk
	public static final String PI_HOSTNAME = "192.168.2.150";
	
	public static void main(String[] args) throws InterruptedException, TimeoutException {
		
		try {
		System.setProperty("java.rmi.server.hostname", PI_HOSTNAME); /* Maak kenbaar dat het RMI-register op dit adres
			 																  gevonden kan worden. */
		LocateRegistry.createRegistry(1099); // maak een register op de opgegeven poort.
		MainProgramImpl zeppelin = new MainProgramImpl();
		Naming.rebind("rmi://localhost:1099/Zeppelin", zeppelin); // maak de zeppelin beschikbaar in het register.
		// TODO: verbinding maken met ResultPointFinder op de client
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
