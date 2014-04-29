package qrcode;

import java.io.UnsupportedEncodingException;
import java.security.*;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;



/**
 * Simple RSA public key encryption algorithm implementation.
 * <P>
 * Taken from "Paj's" website:
 * <TT>http://pajhome.org.uk/crypt/rsa/implementation.html</TT>
 * <P>
 * Adapted by David Brodrick
 */
public class Rsa  {
	  
	
	PrivateKey priv;
	PublicKey pub;
	
	public Rsa() throws NoSuchAlgorithmException {
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
		
		KeyPair pair = keyGen.generateKeyPair();
		this.priv = pair.getPrivate();
		this.pub = pair.getPublic();
	}
	
	public String encode(String input) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
		byte[] dataToEncrypt = input.getBytes();
		byte[] encryptedData = null;  
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, pub); //pubKey stored earlier
		encryptedData = cipher.doFinal(dataToEncrypt);
		String encoded = new String(encryptedData, "UTF-8");
		return encoded;
	}
	
	public String decode(String qrCode) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException {
		byte[] dataToDecrypt = qrCode.getBytes();
		byte[] decryptedData = null;  
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, priv); //privKey stored earlier
		decryptedData = cipher.doFinal(dataToDecrypt);
		String decoded = new String(decryptedData, "UTF-8");		
		return decoded;
	}
	
}