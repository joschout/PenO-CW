package test;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.imageio.ImageIO;

import qrcode.DecodeQR;
import qrcode.RSA;

public class QRTestwithRSA {

	public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
		RSA rsa;
		try {
			rsa = new RSA();
			URL url = new URL("http://192.168.2.134:5000/static/ijzer3.png");
			BufferedImage image = ImageIO.read(url);
			DecodeQR qr = new DecodeQR(rsa);
			System.out.println(qr.decodeImage(image));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

}
