package test;

import java.io.IOException;

import qrcode.DecodeQR;
import controllers.CameraController;

public class QRCodeTest {
	
	public static void main(String[] args) {
		boolean found = false;
		CameraController cam = new CameraController();
		while (! found) {
			System.out.println("En we proberen nog eens...");
			String filename = Long.toString(System.currentTimeMillis());
			try {
				cam.takeRegularPicture(filename, 100);
				DecodeQR read = new DecodeQR();
				String result = read.decodeImage(CameraController.PICTURE_PATH + filename + ".jpg");
				if (result != null) {
					found = true;
				}
				System.out.println("QR-code gevonden met tekst: " + result);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
