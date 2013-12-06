package QRCode;

import java.io.IOException;

import zeppelin.MainProgramImpl;
import client.FTPOrientationIface;

public class Orientation {
	
	private FTPOrientationIface finder;
	
	public double getOrientation(double currentHeight) throws IllegalStateException {
		if (finder == null)
			throw new IllegalStateException("Client is niet beschikbaar voor het vinden van de ori�ntatie.");
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
			throw new IllegalStateException("Kan finder in Orientation niet meer dan ��n keer instanti�ren.");
		this.finder = finder;
	}
	
	public boolean finderSet() {
		return this.finder != null;
	}
	
	

}
