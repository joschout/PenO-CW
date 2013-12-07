/**
 * 
 */

package controllers;

import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;

import ftp.FTPFileInfo;
import zeppelin.MainProgramInterface;

public class CameraController implements Serializable {

	private ImageIcon image;
	
	private static Object lock = new Object();

	/**
	 * Laat de Raspberry Pi een foto nemen
	 * @param	pFileName
	 * 			De naam die de Pi zal geven aan de nieuwe foto. Uiteindelijk zal
	 * 			de naam van de foto de volgende vorm hebben:
	 * 			pFileName.jpg
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public void takePicture(String pFileName, double currentHeight) throws InterruptedException, IOException
	{  
		synchronized (lock) {
			double[] resolution = this.decideResolution(currentHeight);
			executeShellCommand("raspistill -t 1 -w " + resolution[0] + " -h "
					+ resolution[1] + " -o " + FTPFileInfo.PATH_TO_FTP_FILES
					+ pFileName + ".jpg");
			//		executeShellCommand("raspistill -t 1 -w 800 -h 600 -o " + FTPFileInfo.PATH_TO_FTP_FILES+pFileName+".jpg");
		}
	} 

	public ImageIcon getImage() {
		return this.image;
	}

	private void executeShellCommand(String pCommand) throws InterruptedException, IOException  
	{   
		Runtime run = Runtime.getRuntime() ;  
		Process pr = run.exec(pCommand) ;  
		pr.waitFor() ;  
	}
	
	private double[] decideResolution(double currentHeight) {
		double[] toReturn = new double[2];
		if (currentHeight <= 100) {
			toReturn[0] = 400;
			toReturn[1] = 300;
		}
		else if (currentHeight <= 160) {
			toReturn[0] = 800;
			toReturn[1] = 600;
		}
		else {
			toReturn[0] = 1400;
			toReturn[1] = 1050;
		}
		return toReturn;
	}
}
