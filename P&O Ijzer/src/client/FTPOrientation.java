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
	
	private double calcRotation(ResultPoint[] points) {
		ResultPoint a= points[1];
		ResultPoint b= points[2];
		ResultPoint c= points[0];
		//Find the degree of the rotation that is needed

		double x = c.getX()-a.getX();
		double y = c.getY()-a.getY();
		double theta = Math.toDegrees(Math.atan2(x, -y));
		theta += 180;
		if(theta < 0)
			theta += 360;
		return theta;
	}
	
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
	
	

}
