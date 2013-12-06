package ftp;

import java.util.Calendar;

public class LogWriter {

	private static final Object lock = new Object();
	private String log = "";

	private static Calendar calendar = Calendar.getInstance();
	private static int maxLogCharSize = 2000000;

	private boolean MARK_FOR_WIPE = false;

	public void writeToLog(String input) {
		synchronized (lock) {
			if (log.length() >= maxLogCharSize) {
				return;
			}
			int hours = calendar.get(Calendar.HOUR_OF_DAY);
			int minutes = calendar.get(Calendar.MINUTE);
			StringBuilder hourSyntax = new StringBuilder();
			StringBuilder minuteSyntax = new StringBuilder();
			hourSyntax.append(hours);
			minuteSyntax.append(minutes);
			if (hours < 10) {
				hourSyntax.append(0);
				hourSyntax.reverse();
			}
			if (minutes < 10) {
				minuteSyntax.append(0);
				minuteSyntax.reverse();
			}
			if (MARK_FOR_WIPE) {
				MARK_FOR_WIPE = false;
				wipeLog();
			}
			if (log.length() == 0)
				log = hourSyntax.toString() + ":" + minuteSyntax.toString() + " - " + input;
			else log = log + "\n" + hourSyntax.toString() + ":" + minuteSyntax.toString() + " - " + input;
		}
	}

	public String getLog() {
		synchronized(lock) {
			if (log.length() == 0) return null;
			MARK_FOR_WIPE = true;
			return log;
		}
	}

	private void wipeLog() {
		synchronized (lock) {
			log = "";
		}
	}
}