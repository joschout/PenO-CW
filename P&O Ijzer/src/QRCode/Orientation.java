/**
 * Laag tussen de zeppelin en de client. Maakt het mogelijk om de client de
 * oriëntatiehoek van de zeppelin te laten berekenen.
 */

package QRCode;

import java.io.IOException;

import zeppelin.MainProgramImpl;
import client.FTPOrientationIface;

public class Orientation {
	
	private FTPOrientationIface finder;
	
	/**
	 * Laat de client de oriëntatiehoek berekenen. Neem eerst een foto en laat de client
	 * de oriëntatiehoek daaruit gebruiken.
	 * @param currentHeight
	 *        Meest recent gemeten hoogte van de zeppelin. Wordt gebruikt om te bepalen
	 *        op welke resolutie de foto moet worden genomen.
	 * @throws IllegalStateException
	 */
	public double getOrientation(double currentHeight) throws IllegalStateException {
		if (finder == null)
			throw new IllegalStateException("Client is niet beschikbaar voor het vinden van de oriëntatie.");
		try {
			double toReturn = -1;
			String filename;
			while (toReturn == -1) {
				filename = Long.toString(System.currentTimeMillis());
				MainProgramImpl.CAMERA_CONTROLLER.takePicture(filename, currentHeight);
				toReturn = finder.findOrientationFromPicture(filename);
			}
			return toReturn;
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return -1; // abnormale return-waarde
	}
	
	public void setFinder(FTPOrientationIface finder) throws IllegalStateException {
		if (this.finder != null)
			throw new IllegalStateException("Kan finder in Orientation niet meer dan één keer instantiëren.");
		this.finder = finder;
	}
	
	public boolean finderSet() {
		return this.finder != null;
	}
	
	

}
