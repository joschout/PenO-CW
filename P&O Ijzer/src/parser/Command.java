package parser;
import java.rmi.RemoteException;
import java.util.ArrayList;

import client.FTPOrientation;
import zeppelin.MainProgramImpl;
import movement.HeightAdjuster;
import movement.RotationController;
import controllers.MotorController;


public class Command {

	private boolean executed = false;
	
	private FTPOrientation finder;
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

	private void setExecuted() {
		this.executed = true;
	}

	
	
	
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




	private static final long FORWARD_SPEED = 34; // 1 m per 3.42 seconden
	private static final long BACKWARD_SPEED = 74; // 1 m per 7.43 seconden
	private static final long LEFT_SPEED = 38; // nog testen
	private static final long RIGHT_SPEED = 41; // nog testen
	
	private MainProgramImpl zeppelin;
	
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

	private void goBackward() {
		long duration = (long) this.getParameter() * BACKWARD_SPEED;
		long endTime = System.currentTimeMillis() + duration;
		while (System.currentTimeMillis() <= endTime)
		{
			if (! MainProgramImpl.MOTOR_CONTROLLER.movingHorizontally())
			{
				MainProgramImpl.MOTOR_CONTROLLER.backward();
			}
		}
		if (MainProgramImpl.MOTOR_CONTROLLER.goingBackward())
			MainProgramImpl.MOTOR_CONTROLLER.stopRightAndLeftMotor();
	}

	private void goForward() {
		long duration = (long) this.getParameter() * FORWARD_SPEED;
		long endTime = System.currentTimeMillis() + duration;
		while (System.currentTimeMillis() <= endTime)
		{
			if (! MainProgramImpl.MOTOR_CONTROLLER.movingHorizontally())
			{
				MainProgramImpl.MOTOR_CONTROLLER.forward();
			}
		}
		if (MainProgramImpl.MOTOR_CONTROLLER.goingForward())
			MainProgramImpl.MOTOR_CONTROLLER.stopRightAndLeftMotor();
	}
	
	private void goLeft() {
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
		MainProgramImpl.MOTOR_CONTROLLER.writeSoftPwmValues(0, 0, 0, 0);
	}
	
	private void goRight() {
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
		MainProgramImpl.MOTOR_CONTROLLER.writeSoftPwmValues(0, 0, 0, 0);
	}
	
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
