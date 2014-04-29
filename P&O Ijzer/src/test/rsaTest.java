package test;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import qrcode.Rsa;

public class rsaTest {

	public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
		Rsa rsa = new Rsa();
		
		System.out.println(new BigInteger(rsa.getPublicKey().getEncoded()));
		
		String string = "Milan is de beste";
		byte[] encoded = rsa.encode(string);
		
		System.out.println(encoded);
		byte[] decoded = rsa.decode(encoded);
		String output = new String(decoded);
	
		System.out.println(output);

	}

}
