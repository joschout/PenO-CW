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

	private void takePicture(String pFileName)  
	   {  
	     executeShellCommand("raspistill -t 1 -q 5 -o "+pFileName+".jpg");  
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
	 
	 public void imageToObject(String pFileName) {
		 takePicture(pFileName);
		 this.image = new ImageIcon(pFileName+".jpg");
		 
	 }
}
