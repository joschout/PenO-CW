/**
 * Houdt de log bij. Laat toe om in de log te schrijven en om de inhoud van de log op te halen.
 * Ophalen van de inhoud is een destructieve operatie.
 */

package logger;

import java.util.Calendar;

public class LogWriter {

	/**
	 * Dummyobject om mutual exclusion te verzekeren.
	 */
	private static final Object lock = new Object();
	
	private String log = "";

	/**
	 * Om makkelijk een timestamp te kunnen geven aan elk item in de log.
	 */
	private static Calendar calendar = Calendar.getInstance();
	
	/**
	 * Als de log ooit boven deze lengte uitgroeit, is er duidelijk geen interesse in de log.
	 */
	private static int maxLogCharSize = 2000000;

	/**
	 * Geeft aan of bij de volgende schrijfoperatie de log leeg moet worden gemaakt.
	 */
	private boolean MARK_FOR_WIPE = false;

	/**
	 * Schrijft de gegeven input in de log.
	 * @param input
	 *        Nieuwe lijn in de log.
	 */
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
				log = "\n" + hourSyntax.toString() + ":" + minuteSyntax.toString() + " - " + input;
			else log = log + "\n" + hourSyntax.toString() + ":" + minuteSyntax.toString() + " - " + input;
		}
	}

	/**
	 * Haalt de log op en geeft aan dat de log bij de volgende schrijfoperatie eerst
	 * leeg moet worden gemaakt.
	 */
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