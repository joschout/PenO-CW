package test;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import qrcode.RSA;

public class rsaTest {

	public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
		RSA rSA = new RSA();
		
		System.out.println(new BigInteger(rSA.getPublicKey().getEncoded()));
		
		String string = "Milan is de beste";
		byte[] encoded = rSA.encode(string);
		
		System.out.println(encoded);
		byte[] decoded = rSA.decode(encoded);
		String output = new String(decoded);
	
		System.out.println(output);

	}

}
