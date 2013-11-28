package ftp;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

import zeppelin.MainProgramInterface;

public class LogWriter {

	private static final Object lock = new Object();
	
	Calendar calendar = Calendar.getInstance();
	
	public void writeToLog(String input) {
		synchronized (lock) {
			try {
				BufferedWriter writer = new BufferedWriter(new FileWriter(
						FTPFileInfo.PATH_TO_FTP_FILES
								+ FTPFileInfo.LOGFILE_HOSTFILENAME, true));
				writer.append("\n");
				writer.append(calendar.get(Calendar.HOUR_OF_DAY) + ":"
						+ calendar.get(Calendar.MINUTE) + " - " + input);
				writer.close();
			} catch (IOException e) {
				System.err
						.println("Kon logfile niet openen bij het schrijven van volgende input"
								+ input);
			}
		}
	}
}