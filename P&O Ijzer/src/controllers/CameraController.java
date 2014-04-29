/**
 * Laat toe dat de zeppelin de camera aanspreekt.
 */

package controllers;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import org.opencv.highgui.Highgui;

import positioning.Image;

public class CameraController implements Serializable {
	
	public static final String PICTURE_PATH = "/run/shm/";
	private static final Object lock = new Object();

	/**
	 * Laat de Raspberry Pi een foto nemen
	 * @param	pFileName
	 * 			De naam die de Pi zal geven aan de nieuwe foto. Uiteindelijk zal
	 * 			de naam van de foto de volgende vorm hebben:
	 * 			pFileName.jpg
	 * @param   currentHeight
	 * 			De huidige hoogte van de zeppelin. Dit zal bepalen op welke
	 * 			resolutie de foto genomen wordt.
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public Image takePicture(String pFileName) throws InterruptedException, IOException
	{  
		if (pFileName == null)
		{
			throw new IllegalArgumentException("Kan image niet initialiseren met null filePath.");
		}
		synchronized (lock) {
			executeShellCommand("raspistill -t 1 -w " + 500 + " -h "
					+ 500 + " -o " + PICTURE_PATH + pFileName + ".jpg");
		}
		return new Image(Highgui.imread(PICTURE_PATH + pFileName + ".jpg"));
	}
	
	public void takeRegularPicture(String pFileName, double height) throws InterruptedException, IOException
	{
		double[] resolution = this.decideResolution(height);
		synchronized (lock) {
			executeShellCommand("raspistill -t 1 -w " + resolution[0] + " -h "
					+ resolution[1] + " -o " + PICTURE_PATH + pFileName + ".jpg");
		}
	}

	private void executeShellCommand(String pCommand) throws InterruptedException, IOException  
	{   
		Runtime run = Runtime.getRuntime() ;  
		Process pr = run.exec(pCommand) ;  
		pr.waitFor() ;  
	}
	
	private double[] decideResolution(double currentHeight) {
		double[] toReturn = new double[2];
//		if (currentHeight <= 100) {
//			toReturn[0] = 400;
//			toReturn[1] = 300;
//		}
//		else if (currentHeight <= 160) {
//			toReturn[0] = 800;
//			toReturn[1] = 600;
//		}
//		else {
//			toReturn[0] = 1400;
//			toReturn[1] = 1050;
//		}
		toReturn[0] = 800;
		toReturn[1] = 600;
		return toReturn;
	}
}
