/**
 * Deze klasse maakt een Zeppelin object aan en maakt deze beschikbaar in het RMI-register.
 */

package server;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import org.opencv.core.Core;

import controllers.SensorController.TimeoutException;
import zeppelin.MainProgramImpl;

public class ZeppelinServer {
	
	// Afgesproken vaste IP-adres van de zeppelin in de LAN.
	public static final String PI_HOSTNAME = "192.168.2.150";
	
	public static void main(String[] args) throws InterruptedException, TimeoutException {
		//TODO: vraag voor beginpositie
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		MainProgramImpl zeppelin = null;
		try {
			zeppelin = new MainProgramImpl();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		zeppelin.startGameLoop(); // start d
	}

}
