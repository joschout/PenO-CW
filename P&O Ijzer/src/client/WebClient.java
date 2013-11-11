package client;

import it.sauronsoftware.ftp4j.FTPAbortedException;
import it.sauronsoftware.ftp4j.FTPDataTransferException;
import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import ftp.FTPFileInfo;
import ftp.LogFileClient;
import ftp.QRCodeClient;

public class WebClient {
	
	private LogFileClient logClient;
	private QRCodeClient qrCodeClient;
	
	public WebClient() throws IllegalStateException, IOException, FTPIllegalReplyException, FTPException {
		this.logClient = new LogFileClient();
		this.qrCodeClient = new QRCodeClient();
	}
	
	public String readLogFile() throws IllegalStateException, FileNotFoundException, IOException, FTPIllegalReplyException, FTPException, FTPDataTransferException, FTPAbortedException {
		this.logClient.getLogFile();
		BufferedReader reader = new BufferedReader(new FileReader(FTPFileInfo.LOGFILE_LOCALFILENAME));
		StringBuilder builder = new StringBuilder();
		String line = reader.readLine();
		while (line != null) {
			builder.append(line).append("\n");
			line = reader.readLine();
		}
		reader.close();
		return builder.toString();
	}
	
	public BufferedImage getLastScannedImage() throws IllegalStateException, IOException, FTPIllegalReplyException, FTPException, FTPDataTransferException, FTPAbortedException {
		return this.qrCodeClient.getLastScannedImage();
	}

}
