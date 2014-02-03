/**
 * Voor automatische beweging vooruit of achteruit. Laat de motoren draaien tot
 * ofwel de geschatte tijd nodig om de gewenste afstand te overbruggen gepasseerd is
 * of tot er een volgende QR-code is gevonden.
 */

package movement;

import java.io.IOException;
import java.rmi.RemoteException;

import zeppelin.MainProgramImpl;

public class ForwardBackwardController {
	
	private static final long FORWARD_SPEED = 100;
	private static final long BACKWARD_SPEED = 150;
	
	private MainProgramImpl zeppelin;
	
	public void goForward(double distance) {
		try {
			long upperLimit = (long) distance * FORWARD_SPEED;
			long endTime = System.currentTimeMillis() + upperLimit;
			
			zeppelin.goForward();
			while (System.currentTimeMillis() <= endTime && ! correctSeqNum(MainProgramImpl.QR_CODE_READER.tryReadQrCode(zeppelin.sensorReading()))) {
				System.out.println("Vooruit while");
			}
			if (System.currentTimeMillis() > endTime) {
				System.out.println("Lus gebroken door timeout");
			}
			else {
				System.out.println("Lus gebroken door correct volgnummer");
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				zeppelin.stopRightAndLeft();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void goBackward(double distance) {
		try {
			long upperLimit = (long) distance * BACKWARD_SPEED;
			long endTime = System.currentTimeMillis() + upperLimit;

			zeppelin.goBackward();
			while (System.currentTimeMillis() <= endTime && ! correctSeqNum(MainProgramImpl.QR_CODE_READER.tryReadQrCode(zeppelin.sensorReading()))) {
				System.out.println("Achteruit while");
			}
			if (System.currentTimeMillis() > endTime) {
				System.out.println("Lus gebroken door timeout");
			}
			else {
				System.out.println("Lus gebroken door correct volgnummer");
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				zeppelin.stopRightAndLeft();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void setZeppelin(MainProgramImpl zeppelin) {
		this.zeppelin = zeppelin;
	}
	
	private boolean correctSeqNum(String decoded) {
		if (decoded == null)
			return false;
		return decoded.contains("N:" + zeppelin.getExpectedSequenceNumber());
	}
}
