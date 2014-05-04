package test;

import java.io.IOException;

import qrcode.RSA;

public class rsaTest {

	public static void main(String[] args) throws IOException {
		RSA rSA = new RSA();
		
		System.out.println("test");
		String decoded = rSA.decode("Milan is de beste");
	
		System.out.println(decoded);

	}

}
