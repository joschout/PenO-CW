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
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void takePicture(String pFileName)  
	   {  
	     executeShellCommand("raspistill -t 1 -w 540 -h 400 -o "+pFileName+".jpg");  
	   } 
	
	public ImageIcon getImage() {
		return this.image;
	}
	 
	 private void executeShellCommand(String pCommand)  
	   {  
	     try {  
	       Runtime run = Runtime.getRuntime() ;  
	       Process pr = run.exec(pCommand) ;  
	       pr.waitFor() ;  
	     } catch (IOException ex) {  
	       Logger.getLogger(CameraController.class.getName()).log(Level.SEVERE, null, ex);  
	     } catch (InterruptedException ex) {
		   Logger.getLogger(CameraController.class.getName()).log(Level.SEVERE, null, ex);  
	    	 
	     }
	   }  
	 
	 /**
	  * Obsolete.
	  * @param pFileName
	  */
	 public void imageToObject(String pFileName) {
		 takePicture(pFileName);
		 this.image = new ImageIcon(pFileName+".jpg");
		 
	 }
}
