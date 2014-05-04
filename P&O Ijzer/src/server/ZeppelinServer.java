
/**
 * Deze klasse maakt een Zeppelin object aan en maakt deze beschikbaar in het RMI-register.
 */

package server;

import java.io.IOException;
import java.net.MalformedURLException;



import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

import org.opencv.core.Core;

import controllers.SensorController.TimeoutException;
import coordinate.GridPoint;
import zeppelin.MainProgramImpl;

public class ZeppelinServer {
	
	// Afgesproken vaste IP-adres van de zeppelin in de LAN.
	public static final String PI_HOSTNAME = "192.168.2.150";
	
	public static void main(String[] args) throws InterruptedException, TimeoutException, NoSuchAlgorithmException, IOException {
		//TODO: vraag voor beginpositie
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		Scanner scanner = new Scanner(System.in);
		System.out.println("Geef initiële positie (<x>,<y>)");
		String pos = scanner.nextLine();
		String[] posParts = pos.split(",");
		double x = Double.parseDouble(posParts[0]);
		double y = Double.parseDouble(posParts[1]);
		
		MainProgramImpl zeppelin = null;
		
			zeppelin = new MainProgramImpl(new GridPoint(x,y));

			
		// TODO for testing purposes, doe weg erna
		zeppelin.startGameLoop(); // start d
	}

}
