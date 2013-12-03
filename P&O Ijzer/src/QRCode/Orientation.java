package QRCode;

import java.io.IOException;

import zeppelin.MainProgramImpl;
import client.ResultPointFinderInterface;

public class Orientation {
	
	private ResultPointFinderInterface finder;
	
	public double getOrientation(double currentHeight) throws IllegalStateException {
		if (finder == null)
			throw new IllegalStateException("Client is niet beschikbaar voor het vinden van de oriëntatie.");
		String filename = Long.toString(System.currentTimeMillis());
		try {
			MainProgramImpl.CAMERA_CONTROLLER.takePicture(filename, currentHeight);
			return finder.findOrientationFromPicture(filename);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return -1; // abnormale return-waarde
	}
	
	public void setFinder(ResultPointFinderInterface finder) throws IllegalStateException {
		if (this.finder != null)
			throw new IllegalStateException("Kan finder in Orientation niet meer dan één keer instantiëren.");
		this.finder = finder;
	}
	
	public boolean finderSet() {
		return this.finder != null;
	}
	
	

}
