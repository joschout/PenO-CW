package QRCode;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;


import javax.imageio.ImageIO;

import com.google.zxing.*;
import com.google.zxing.common.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;



public class Test1QRcode {
	public static void main(String args[]){
		try {
			InputStream barCodeInputStream = new FileInputStream("/home/r0294084/Downloads/qrcode1.png");  
			BufferedImage barCodeBufferedImage = ImageIO.read(barCodeInputStream);  
  
			LuminanceSource source = new BufferedImageLuminanceSource(barCodeBufferedImage);  
			BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));  
			Reader reader = new MultiFormatReader();  
			Result result = reader.decode(bitmap);  
  
			System.out.println("Barcode text is " + result.getText());
		} catch ( Exception e) {
			e.printStackTrace();
}  
 }
 
 
}