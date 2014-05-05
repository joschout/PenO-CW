
package qrcode;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;


public class RSAWindows implements RSAInterface {

	byte[] pub;

	public RSAWindows() throws IOException {
		Process p = Runtime.getRuntime().exec("cmd /c /wait python keys.py");
		try {
			p.waitFor();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		this.pub = this.readPublicKey();
		System.out.println("Key gemaakt: " + new String(pub));
	}
	public byte[] getPublicKey() {
		return this.pub;
	}

	public void encode(String input) {
		FileWriter fstream = null;
		BufferedWriter out = null;
		PrintWriter writer = null;
		
		File file = new File("to_encrypt");
		try {
			fstream = new FileWriter(file, false);
			out = new BufferedWriter(fstream);
			writer = new PrintWriter(out);
			writer.write(input);
			writer.close();
			Runtime.getRuntime().exec("cmd /c python encrypt.py");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String decode() throws IOException {
		Process p = Runtime.getRuntime().exec("cmd /c /wait python decription.py");
		try {
			p.waitFor();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		String decryptedData = read("result");

		return decryptedData;
	}

	public String read(String fileName){
		String result = "";
		try {
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				sb.append(System.lineSeparator());
				line = br.readLine();
			}
			result = sb.toString();
			br.close();
		} catch(IOException e) {
		}
		return result;
	}

	public void write(String fileName, String text) throws FileNotFoundException, UnsupportedEncodingException{
		PrintWriter writer;
		writer = new PrintWriter("encrypted", "UTF-8");
		writer.println(text);
		writer.close();
	}
	
	public byte[] readPublicKey() throws IOException {
		File file = new File("public");
		FileInputStream stream = new FileInputStream(new File("public"));
		byte[] bytes = new byte[(int) file.length()];
		int offset = 0;
		stream.read(bytes, offset, (int) file.length());
		stream.close();
		return bytes;
	}

}
