package client;

import java.io.IOException;

import server.ZeppelinServer;
import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;

public class WebClient {
	
	private FTPClient QRFileClient;
	
	public WebClient() throws IllegalStateException, IOException, FTPIllegalReplyException, FTPException {
		this.QRFileClient = new FTPClient();
		this.QRFileClient.connect(ZeppelinServer.host);
		this.QRFileClient.login("pi", "ijzerisawesome");
		this.QRFileClient.setAutoNoopTimeout(30000);
	}
	
	public void getFile(String fileNameOnHost, String fileNameOnLocal) {
		
	}

}
