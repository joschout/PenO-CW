package QRCode;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;


public class Test2QRcode {
	// create ImageIcon with QRcode
	private static ImageIcon icon = new ImageIcon("/home/r0294084/Downloads/qrcode1.png");
	
	// save ImageIcon to file
	public static void save(){
		Image img = icon.getImage();

		BufferedImage bi = new BufferedImage(img.getWidth(null),img.getHeight(null),BufferedImage.TYPE_INT_ARGB);

		Graphics2D g2 = bi.createGraphics();
		g2.drawImage(img, 0, 0, null);
		g2.dispose();
		try {
			ImageIO.write(bi, "png", new File("/home/r0294084/Downloads/img.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// read QRcode from file
	public static void read(){
		try {
			InputStream barCodeInputStream = new FileInputStream("/home/r0294084/Downloads/img.png");  
			BufferedImage barCodeBufferedImage = ImageIO.read(barCodeInputStream);  
  
			LuminanceSource source = new BufferedImageLuminanceSource(barCodeBufferedImage);  
			BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));  
			Reader reader = new MultiFormatReader();  
			Result result = reader.decode(bitmap);  
  
			System.out.println("Barcode text is " + result.getText());
		} catch ( Exception e) {
				// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	}
	
	public static void main(String args[]){
		save();
		read();
 }
}
