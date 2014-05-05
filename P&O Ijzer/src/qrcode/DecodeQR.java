
package qrcode;


import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.imageio.ImageIO;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

public class DecodeQR {

	RSAInterface rsa;

	public DecodeQR(RSAInterface rsa) {
		this.rsa = rsa;
	}

	public String decodeImage(String fileName) throws IOException {

		try{

			BufferedImage image = ImageIO.read(new FileInputStream(fileName));
			return this.decodeImage(image);

		}catch(Exception ex){
			ex.printStackTrace();

		}
		return null;
		
	}
	
	public String decodeImage(BufferedImage image) {
		Result result = null;
		BinaryBitmap binaryBitmap;

		try{

			binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(image)));
			result = new MultiFormatReader().decode(binaryBitmap);
			String codedCommand = result.getText();
			System.out.println("Gelezen: " + codedCommand);
			this.writeEncrypted(codedCommand);
			String toReturn = rsa.decode();
			System.out.println("Gedecrypteerd: " + toReturn);
			return toReturn;

		}catch(Exception ex){
			ex.printStackTrace();

		}
		return null;
	}
	
	private void writeEncrypted(String encrypted) {
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
	

}


