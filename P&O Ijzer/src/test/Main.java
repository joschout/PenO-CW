package test;

import java.io.IOException;

import org.opencv.core.Core;


public class Main {


	
	/**
	 * @param args
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException, IOException {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		Test test = new Test();
	}
	
	

}
