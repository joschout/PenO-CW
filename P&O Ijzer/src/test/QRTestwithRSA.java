package test;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import qrcode.DecodeQR;
import qrcode.RSA;

public class QRTestwithRSA {

	public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
		RSA rsa = new RSA();
		byte[] coded = rsa.encode("positie");
		
		System.out.println(new BigInteger(coded));
		
		String result =new String( rsa.decode(coded));
		System.out.println(result);

	}

}
