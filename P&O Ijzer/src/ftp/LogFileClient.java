/**
 * Deprecated.
 */

package ftp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import server.ZeppelinServer;
import it.sauronsoftware.ftp4j.FTPAbortedException;
import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferException;
import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;

public class LogFileClient {
	
	private FTPClient ftpClient;
	
	public LogFileClient() throws IllegalStateException, IOException, FTPIllegalReplyException, FTPException {
		ftpClient = new FTPClient();
		this.ftpClient.connect(ZeppelinServer.PI_HOSTNAME);
		this.ftpClient.login("pi", "ijzerisawesome");
		this.ftpClient.setAutoNoopTimeout(30000);
	}
	
	public void getLogFile() throws IllegalStateException, FileNotFoundException, IOException, FTPIllegalReplyException, FTPException, FTPDataTransferException, FTPAbortedException {
		ftpClient.download(FTPFileInfo.CLIENT_DOWNLOAD_PATH + FTPFileInfo.LOGFILE_HOSTFILENAME, new File(FTPFileInfo.LOGFILE_LOCALFILENAME));
	}
}
