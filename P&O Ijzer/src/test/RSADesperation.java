package test;

import java.io.IOException;

import qrcode.RSAInterface;
import qrcode.RSAWindows;

public class RSADesperation {

	public static void main(String[] args) throws IOException {
		RSAWindows rsa = new RSAWindows();
		System.out.println(rsa.getPublicKey());
		System.out.println(rsa.getPrivateKey());
	}

}
