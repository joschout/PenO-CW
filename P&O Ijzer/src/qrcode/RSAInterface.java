
package qrcode;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;


public interface RSAInterface  {

	public byte[] getPublicKey();

	public void encode(String input);

	public String decode() throws IOException;

	public String read(String fileName);

	public void write(String fileName, String text) throws FileNotFoundException, UnsupportedEncodingException;
	
	public byte[] readPublicKey() throws IOException;

}
