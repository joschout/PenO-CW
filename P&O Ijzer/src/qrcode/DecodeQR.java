package qrcode;

import java.io.FileInputStream;
import javax.imageio.ImageIO;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

public class DecodeQR {

	private String decodeString;

	public DecodeQR(String fileName) {
		decodeString = decodeImage(fileName);
	}

	private String decodeImage(String fileName) {
		Result result = null;
		BinaryBitmap binaryBitmap;

		try{

			binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(ImageIO.read(new FileInputStream(fileName)))));
			result = new MultiFormatReader().decode(binaryBitmap);

		}catch(Exception ex){
			ex.printStackTrace();
		}
		return result.getText();
	}
	
	public String getDecodedString() {
		return this.decodeString;
	}

}
