/**
 * Abstracte voorstelling van één enkele instructie in een QR-code.
 */

package parser;
import java.rmi.RemoteException;

import client.FTPOrientation;
import zeppelin.MainProgramImpl;
import movement.RotationController;


public class Command {

	private boolean executed = false;
	
	/**
	 * Voert dit commando uit. De acties die de zeppelin zal nemen verschillen naargelang
	 * het type commando dit is.
	 * @throws IllegalStateException
	 */
	public void execute() throws IllegalStateException {
		if (this.isExecuted())
			throw new IllegalStateException("Probeerde commando " + this.toString() + " meermaals uit te voeren!");
		if (this.getType() == CommandType.V) { // vooruit
			goForward();
		}
		else if (this.getType() == CommandType.A) { // achteruit
			goBackward();
		}
		else if (this.getType() == CommandType.S) { // stijgen
			ascend();
		}
		else if (this.getType() == CommandType.D) { // dalen
			descend();
		}
		else if (this.getType() == CommandType.L) { // links
			goLeft();
		}
		else if (this.getType() == CommandType.R){// rechts
			goRight();
		
		}
		else this.setExecuted();
	}

	/**
	 * Geef aan dat dit commando is uitgevoerd.
	 */
	private void setExecuted() {
		this.executed = true;
	}

	/**
	 * Geeft aan of dit commando is uitgevoerd.
	 */
	public boolean isExecuted() {
		return this.executed;
	}




	private CommandType type;
	public CommandType getType() {
		return type;
	}

	public void setType(CommandType type) {
		this.type = type;
	}




	private double parameter;
	public double getParameter() {
		return parameter;
	}

	public void setParameter(Double parameter) {
		this.parameter = parameter;
	}
	
	private MainProgramImpl zeppelin;
	
	/**
	 * Laat de zeppelin stijgen. Ga pas verder naar het volgende commando als de doelhoogte is bereikt.
	 */
	private void ascend() {
		try {
			zeppelin.setTargetHeight(zeppelin.sensorReading() + this.getParameter());
			while (! MainProgramImpl.HEIGHT_ADJUSTER.isInInterval(zeppelin.sensorReading(),
					zeppelin.getTargetHeight())) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Laat de zeppelin dalen. Ga pas verder naar het volgende commando als de doelhoogte is bereikt.
	 */
	private void descend() {
		try {
			zeppelin.setTargetHeight(zeppelin.sensorReading() - this.getParameter());
			while (! MainProgramImpl.HEIGHT_ADJUSTER.isInInterval(zeppelin.sensorReading(),
					zeppelin.getTargetHeight())) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Laat de zeppelin vooruit gaan volgens de parameter meegegeven aan dit commando.
	 */
	private void goBackward() {
		MainProgramImpl.FORWARD_BACKWARD.goBackward(this.getParameter());
	}

	/**
	 * Laat de zeppelin achteruit gaan volgens de parameter meegegeven aan dit commando.
	 */
	private void goForward() {
		MainProgramImpl.FORWARD_BACKWARD.goForward(this.getParameter());
	}
	
	/**
	 * Laat de zeppelin naar links draaien volgens de parameter meegegeven aan dit commando.
	 * Ga pas verder naar het volgende commando als de doelhoek is bereikt.
	 */
	private void goLeft() {
		double currentAngle = requestAngleAndUpdate();
		this.zeppelin.setTargetAngle(RotationController.convertToCorrectFormat(currentAngle - this.getParameter()));
		System.out.println("Huidige hoek: " + currentAngle + " ; doelhoek: " + this.zeppelin.getTargetAngle());
		this.zeppelin.setTurning(true);
		while (! MainProgramImpl.ROTATION_CONTROLLER.isInInterval(this.zeppelin.getMostRecentAngle(), this.zeppelin.getTargetAngle()))
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		this.zeppelin.setTurning(false);
		MainProgramImpl.MOTOR_CONTROLLER.stopRightAndLeftMotor();
	}
	
	/**
	 * Laat de zeppelin naar rechts draaien volgens de parameter meegegeven aan dit commando.
	 * Ga pas verder naar het volgende commando als de doelhoek is bereikt.
	 */
	private void goRight() {
		double currentAngle = requestAngleAndUpdate();
		this.zeppelin.setTargetAngle(RotationController.convertToCorrectFormat(currentAngle + this.getParameter()));
		System.out.println("Huidige hoek: " + currentAngle + " ; doelhoek: " + this.zeppelin.getTargetAngle());
		this.zeppelin.setTurning(true);
		while (! MainProgramImpl.ROTATION_CONTROLLER.isInInterval(this.zeppelin.getMostRecentAngle(), this.zeppelin.getTargetAngle()))
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		this.zeppelin.setTurning(false);
		MainProgramImpl.MOTOR_CONTROLLER.stopRightAndLeftMotor();
	}
	
	/**
	 * Vraag aan de client om de oriëntatiehoek te berekenen. Laat de zeppelin zijn
	 * meest recent gemeten hoek updaten met het resultaat. 0 wordt als standaardwaarde
	 * meegegeven als er geen hoek gemeten kon worden.
	 */
	private double requestAngleAndUpdate() {
		try {
			double mostRecentAngle = MainProgramImpl.ORIENTATION.getOrientation(zeppelin.sensorReading());
			this.zeppelin.updateMostRecentAngle(mostRecentAngle);
			return mostRecentAngle;
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public Command(CommandType type, double parameter, MainProgramImpl zeppelin){
		this.type=type;
		this.parameter=parameter;
		this.zeppelin = zeppelin;
	} 
	
	public String toString() {
		return "| Commandotype: " + this.getType().getCommandAbbrevString() + " ; Parameter: " + this.getParameter() + " |";
	}
}
