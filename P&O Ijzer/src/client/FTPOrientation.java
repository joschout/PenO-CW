/**
 * Implementatie van FTPOrientationInterface.
 */

package client;

import it.sauronsoftware.ftp4j.FTPAbortedException;
import it.sauronsoftware.ftp4j.FTPDataTransferException;
import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import javax.imageio.ImageIO;

import zeppelin.MainProgramImpl;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.FormatException;
import com.google.zxing.LuminanceSource;
import com.google.zxing.NotFoundException;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.detector.Detector;

public class FTPOrientation extends UnicastRemoteObject implements FTPOrientationIface {

	/**
	 * Om files met QR-codes te halen van de zeppelin.
	 */
	private WebClient client;
	
	public FTPOrientation(WebClient client) throws RemoteException {
		super();
		this.client = client;
		
	}
	
	@Override
	public double findOrientationFromPicture(String filename)
			throws RemoteException {
		ResultPoint[] points = this.findResultPoints(filename);
		return this.calcRotation(points);
		
	}
	
	/**
	 * Berekent de oriëntatiehoek van de zeppelin uit de gegeven array van oriëntatiepunten.
	 * @param points
	 * 		  Array van oriëntatiepunten op de QR-code. Dit zijn de drie grote zwarte vlakken
	 * 		  in de linkerboven-, rechterboven- en linkerbenedenhoek op de QR-code.
	 * @return
	 */
	private double calcRotation(ResultPoint[] points) {
		if (points == null)
			return -1;
		ResultPoint a= points[1]; // linkerboven QR-code oriëntatiepunt.
		ResultPoint b= points[2]; // rechterboven QR-code oriëntatiepunt.
		ResultPoint c= points[0]; // linkerbeneden QR-code oriëntatiepunt.
		//Find the degree of the rotation that is needed

		double x = c.getX()-a.getX();
		double y = c.getY()-a.getY();
		double theta = Math.toDegrees(Math.atan2(x, -y));
		
		// Hou er rekening mee dat de zeppelin 90 graden is gedraaid tegenover
		// de camera.
		return (-theta + 270) % 360;
	}
	
	/**
	 * Zoek de oriëntatiepunten van een QR-code uit de opgegeven file.
	 * @param filename
	 *        Fotobestand waaruit oriëntatiepunten gevonden moeten worden.
	 * @return
	 */
	private ResultPoint[] findResultPoints(String filename) {
		try {
			BufferedImage image = client.getImageFromFile(filename);
			LuminanceSource source = new BufferedImageLuminanceSource(image);
			BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
			Detector detector = new Detector(bitmap.getBlackMatrix());
			return detector.detect().getPoints();
		} catch (NotFoundException e) {
			e.printStackTrace();
		} catch (FormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (FTPIllegalReplyException e) {
			e.printStackTrace();
		} catch (FTPException e) {
			e.printStackTrace();
		} catch (FTPDataTransferException e) {
			e.printStackTrace();
		} catch (FTPAbortedException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public double getAngleComplement(double angle) {
		return 360 - angle;
	}
	
	

}
