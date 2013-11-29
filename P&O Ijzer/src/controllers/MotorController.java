package controllers;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import com.pi4j.io.gpio.GpioPinPwmOutput;
import com.pi4j.io.gpio.RaspiPin;

import zeppelin.MainProgramImpl;
import components.Motor;
import ftp.LogWriter;

public class MotorController implements Serializable {
	
	private static final GpioPinPwmOutput PWMPin = MainProgramImpl.gpio.provisionPwmOutputPin(RaspiPin.GPIO_01);
	private static final int MINIMUM_RESPONSE = 800;
	private static final int MAXIMUM_RESPONSE = 1024;
	private static final int INTERVAL_LENGTH = MAXIMUM_RESPONSE - MINIMUM_RESPONSE;
	
	private Motor leftMotor;
	private Motor rightMotor;
	private Motor downwardMotor;
	
	private static final LogWriter logWriter = new LogWriter();
	
	// Motor 1: GPIO_05 en GPIO_07
	// Motor 2: GPIO_00 en GPIO_04
	// Motor 3: GPIO_13 en GPIO_11
	// Motor 4: GPIO_12 en GPIO_14
	public MotorController() {
		leftMotor = new Motor(RaspiPin.GPIO_11, RaspiPin.GPIO_13); // motor 3
		rightMotor = new Motor(RaspiPin.GPIO_04, RaspiPin.GPIO_00); // motor 2
		downwardMotor = new Motor(RaspiPin.GPIO_14, RaspiPin.GPIO_12); // motor 4
	}
	
	public static void setSpeed(int percentage) {
		if (PWMPin.getPwm() != percentage) {
			logWriter.writeToLog("Motoren laten draaien aan percentage: "
					+ percentage);
			if (percentage == 0) {
				PWMPin.setPwm(0);
				return;
			}
			double factor = (double) percentage / 100;
			int result = (int) factor * INTERVAL_LENGTH + MINIMUM_RESPONSE;
			PWMPin.setPwm(result);
		}
	}
	
	public void left() {
		if (! this.goingLeft()) {
			logWriter.writeToLog("Zeppelin naar links laten draaien.");
			this.leftMotor.counterClockwise();
			this.rightMotor.clockwise();
		}
	}
	
	public void right() {
		if (! this.goingRight()) {
			logWriter.writeToLog("Zeppelin naar rechts laten draaien.");
			this.rightMotor.counterClockwise();
			this.leftMotor.clockwise();
		}
	}

	
	
	public void stopRightAndLeftMotor() {
		if (leftMotor.isOn() && rightMotor.isOn()) {
			logWriter.writeToLog("Zeppelin de linker- en rechtermotor laten stoppen.");
			this.rightMotor.stop();
			this.leftMotor.stop();
		}
	}
	
	public void forward() {
		if (! goingForward()) {
			logWriter.writeToLog("Zeppelin vooruit laten gaan.");
			this.leftMotor.clockwise();
			this.rightMotor.clockwise();
		}
	}
	
	public void backward() {
		if (! goingBackward()) {
			logWriter.writeToLog("Zeppelin achteruit laten gaan.");
			this.leftMotor.counterClockwise();
			this.rightMotor.counterClockwise();
		}
	}
	
	public void up() {
		if (! downwardMotor.goingClockwise()) {
			logWriter.writeToLog("Zeppelin laten stijgen.");
			this.downwardMotor.clockwise();
		}
	}
	
	public void down() {
		if (! downwardMotor.goingCounterClockwise()) {
			logWriter.writeToLog("Zeppelin laten dalen.");
			this.downwardMotor.counterClockwise();
		}
	}
	
	public void stopHeightAdjustment() {
		if (downwardMotor.isOn()) {
			logWriter.writeToLog("Zeppelin laten stoppen met zijn hoogte aan te passen.");
			this.downwardMotor.stop();
		}
	}
	
	public ArrayList<Motor> getMotors() {
		ArrayList<Motor> toReturn = new ArrayList<Motor>();
		toReturn.add(leftMotor);
		toReturn.add(rightMotor);
		toReturn.add(downwardMotor);
		return toReturn;
	}
	
	public boolean leftIsOn() {
		return this.leftMotor.isOn();
	}
	
	public boolean rightIsOn() {
		return this.rightMotor.isOn();
	}
	
	public boolean downwardIsOn() {
		return this.downwardMotor.isOn();
	}
	
	public boolean goingLeft() {
		return this.leftMotor.goingCounterClockwise() && this.rightMotor.goingClockwise();
	}
	
	public boolean goingRight() {
		return this.leftMotor.goingClockwise() && this.rightMotor.goingCounterClockwise();
	}

	public boolean goingForward() {
		return this.leftMotor.goingClockwise() && this.rightMotor.goingClockwise();
	}
	
	public boolean goingBackward() {
		return this.leftMotor.goingCounterClockwise() && this.rightMotor.goingCounterClockwise();
	}
	
	public boolean movingHorizontally() {
		return this.goingForward() || this.goingBackward() || this.goingLeft() || this.goingRight();
	}
}
