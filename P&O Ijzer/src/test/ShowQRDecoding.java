package test;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;

import javax.imageio.ImageIO;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

import controllers.CameraController;
import qrcode.RSAWindows;

public class ShowQRDecoding {

	public static void main(String[] args) throws InterruptedException, IOException {
		CameraController cam = new CameraController();
		String result = null;
		while (result == null) {
			cam.takeRegularPicture("qrcodepic", 120);
			result = decodeImage("/run/shm/qrcodepic.jpg");
		}
		System.out.println("Resultaat: " + result);
	}
	
	public static String decodeImage(String fileName) throws IOException {
		try{

			BufferedImage image = ImageIO.read(new FileInputStream(fileName));
			return decodeImage(image);

		}catch(Exception ex){
			ex.printStackTrace();

		}
		return null;
		
	}
	
	public static String decodeImage(BufferedImage image) {
		Result result = null;
		BinaryBitmap binaryBitmap;

		try{
			binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(image)));
			result = new MultiFormatReader().decode(binaryBitmap);
			String codedCommand = result.getText();
			System.out.println("Gelezen: " + codedCommand);
			writeEncrypted(codedCommand);
			Process p = Runtime.getRuntime().exec("python decription.py");
			p.waitFor();
			String toReturn = read("result");
			System.out.println("Gedecrypteerd: " + toReturn);
			return toReturn;

		}catch(Exception ex){
			ex.printStackTrace();

		}
		return null;
	}
	
	public static void writeEncrypted(String encrypted) {
		FileOutputStream fstream = null;
		OutputStreamWriter out = null;
		
		File file = new File("encrypted");
		try {
			fstream = new FileOutputStream(file, false);
			out = new OutputStreamWriter(fstream, "UTF8");
			out.write(encrypted);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String read(String fileName){
		String result = "";
		try {
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				sb.append(System.lineSeparator());
				line = br.readLine();
			}
			result = sb.toString();
			br.close();
		} catch(IOException e) {
		}
		return result;
	}

}
