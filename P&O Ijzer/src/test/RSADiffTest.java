package test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class RSADiffTest {

	public static void main(String[] args) {
		String read = read("to_encrypt_2");
		System.out.println(read);
	}

	public static String read(String fileName){
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
}
