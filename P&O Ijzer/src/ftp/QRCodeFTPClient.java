/**
 * Verzorgt alle communicatie omtrent het downloaden van QR-codes tussen client en zeppelin.
 */

package ftp;

import it.sauronsoftware.ftp4j.FTPAbortedException;
import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferException;
import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import javax.imageio.ImageIO;

import server.ZeppelinServer;

public class QRCodeFTPClient {
	
private FTPClient QRFileClient;
	
	public QRCodeFTPClient() throws IllegalStateException, IOException, FTPIllegalReplyException, FTPException {
		this.QRFileClient = new FTPClient();
		this.QRFileClient.connect(ZeppelinServer.PI_HOSTNAME);
		this.QRFileClient.login("pi", "ijzerisawesome");
		this.QRFileClient.setAutoNoopTimeout(30000);
	}
	
	/**
	 * Downloadt de gegeven file van de zeppelin en slaagt het op onder de gegeven naam.
	 * @param fileNameOnHost
	 *        Naam van de file op de zeppelin.
	 * @param fileNameOnLocal
	 *        Naam waaronder de inhoud van file moet worden opgeslagen op de client.
	 * @throws IllegalStateException
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws FTPIllegalReplyException
	 * @throws FTPException
	 * @throws FTPDataTransferException
	 * @throws FTPAbortedException
	 */
	private void getFile(String fileNameOnHost, String fileNameOnLocal) throws IllegalStateException, FileNotFoundException, IOException, FTPIllegalReplyException, FTPException, FTPDataTransferException, FTPAbortedException {
		this.QRFileClient.download(FTPFileInfo.CLIENT_DOWNLOAD_PATH + fileNameOnHost, new File(fileNameOnLocal));
	}
	
	/**
	 * Downloadt de timestamp file van de zeppelin.
	 * @throws IllegalStateException
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws FTPIllegalReplyException
	 * @throws FTPException
	 * @throws FTPDataTransferException
	 * @throws FTPAbortedException
	 */
	private void getTimestampFile() throws IllegalStateException, FileNotFoundException, IOException, FTPIllegalReplyException, FTPException, FTPDataTransferException, FTPAbortedException {
		this.QRFileClient.download(FTPFileInfo.CLIENT_DOWNLOAD_PATH + FTPFileInfo.TIMESTAMPLIST_HOSTFILENAME, new File(FTPFileInfo.TIMESTAMPLIST_LOCALFILENAME));
	}
	
	/**
	 * Bepaalt de naam van de meest recent gescande QR-code en welke instructies
	 * daarin zijn geëncodeerd.
	 * @return String array van lengte 2 met volgende structuur:
	 *         op plaats 0 staat de naam waaronder de file is opgeslagen op de zeppelin,
	 *         op plaats 1 staan de instructies die geëncodeerd zijn in de QR-code.
	 * @throws IOException
	 * @throws IllegalStateException
	 * @throws FTPIllegalReplyException
	 * @throws FTPException
	 * @throws FTPDataTransferException
	 * @throws FTPAbortedException
	 */
	public String[] getLastScannedImageInfo() throws IOException, IllegalStateException, FTPIllegalReplyException, FTPException, FTPDataTransferException, FTPAbortedException {
		this.getTimestampFile();
		File file = new File(FTPFileInfo.TIMESTAMPLIST_LOCALFILENAME);
		if (file.length() == 0) {
			return null;
		}
		RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
        StringBuilder builder = new StringBuilder();
        long length = file.length();
        length--;
        randomAccessFile.seek(length);
        for(long seek = length; seek >= 0; --seek){
            randomAccessFile.seek(seek);
            char c = (char)randomAccessFile.read();
            if (c != '\n') builder.append(c);
            else {
                builder = builder.reverse();
                break;
            }

        }
        randomAccessFile.close();
        String[] split = builder.toString().split(",");
        return split;
	}
	
	/**
	 * Maakt een BufferedImage object van het beeldbestand onder de gegeven naam.
	 * @param fileName
	 *        Naam van het beeldbestand op de zeppelin.
	 * @throws IOException
	 * @throws IllegalStateException
	 * @throws FTPIllegalReplyException
	 * @throws FTPException
	 * @throws FTPDataTransferException
	 * @throws FTPAbortedException
	 */
	public BufferedImage getImageFromFile(String fileName) throws IOException, IllegalStateException, FTPIllegalReplyException, FTPException, FTPDataTransferException, FTPAbortedException {
		System.out.println("getImageFromFile argument: " + fileName + " met QRCodeFTPClient: " + this);
		this.getFile(fileName + ".jpg", fileName + ".jpg");
		File file = new File(fileName + ".jpg");
		return ImageIO.read(file);
	}

}
