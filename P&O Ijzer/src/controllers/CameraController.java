/**
 * 
 */

package controllers;

import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;

public class CameraController implements Serializable {

	private ImageIcon image;

	/**
	 * Laat de Raspberry Pi een foto nemen
	 * @param	pFileName
	 * 			De naam die de Pi zal geven aan de nieuwe foto. Uiteindelijk zal
	 * 			de naam van de foto de volgende vorm hebben:
	 * 			pFileName.jpg
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public void takePicture(String pFileName) throws InterruptedException, IOException  
	{  
		executeShellCommand("raspistill -t 1 -w 540 -h 400 -o "+pFileName+".jpg");  
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
}
