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

	/**
	 * Obsolete.
	 * @param pFileName
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public void imageToObject(String pFileName) throws InterruptedException, IOException {
		takePicture(pFileName);
		this.image = new ImageIcon(pFileName+".jpg");

	}
}
