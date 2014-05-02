package qrcode;

import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.imageio.ImageIO;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

public class DecodeQR {

	RSA rsa;

	public DecodeQR(RSA rsa) {
		this.rsa = rsa;
	}

	public String decodeImage(String fileName) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException {

		Result result = null;
		BinaryBitmap binaryBitmap;

		try{

			binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(ImageIO.read(new FileInputStream(fileName)))));
			result = new MultiFormatReader().decode(binaryBitmap);

		}catch(Exception ex){
			ex.printStackTrace();

		}
		String codedCommand = result.getText();
		byte[] b = codedCommand.getBytes();
		byte[] toReturnArray = rsa.decode(b);
		String toReturn = new String(toReturnArray);
		return toReturn;
	}
	

}

