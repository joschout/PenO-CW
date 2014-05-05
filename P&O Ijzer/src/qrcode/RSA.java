
package qrcode;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;


public class RSA  {

	String priv;
	String pub;

	public RSA() throws IOException {
		Runtime.getRuntime().exec("python keys.py");		
		this.priv = read("private");
		this.pub = read("public");
	}

	public String getPrivateKey() {
		return this.priv;
	}

	public String getPublicKey() {
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
			Runtime.getRuntime().exec("python encrypt.py");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String decode(String input) throws IOException {
		write("encrypted", input);
		Runtime.getRuntime().exec("python decryption.py");
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
	
	

}
