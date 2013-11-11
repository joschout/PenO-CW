package controllers;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import com.pi4j.io.gpio.GpioPinPwmOutput;
import com.pi4j.io.gpio.RaspiPin;

import zeppelin.Zeppelin;
import components.Motor;
import ftp.LogWriter;

public class MotorController implements Serializable {
	
	private static final GpioPinPwmOutput PWMPin = Zeppelin.gpio.provisionPwmOutputPin(RaspiPin.GPIO_01);
	private static final int MINIMUM_RESPONSE = 800;
	private static final int MAXIMUM_RESPONSE = 1024;
	private static final int INTERVAL_LENGTH = MAXIMUM_RESPONSE - MINIMUM_RESPONSE;
	
	private Motor leftMotor;
	private Motor rightMotor;
	private Motor downwardMotor;
	
	private static final LogWriter logWriter = new LogWriter();
	
	public MotorController() {
		leftMotor = new Motor(RaspiPin.GPIO_05, RaspiPin.GPIO_07); // motor 1
		rightMotor = new Motor(RaspiPin.GPIO_00, RaspiPin.GPIO_04); // motor 2
		downwardMotor = new Motor(RaspiPin.GPIO_13, RaspiPin.GPIO_11); // motor 3
	}
	
	public static void setSpeed(int percentage) {
		logWriter.writeToLog("Motoren laten draaien aan percentage: " + percentage);
		if (percentage == 0) {
			PWMPin.setPwm(0);
			return;
		}
		double factor = (double) percentage / 100;
		int result = (int) factor * INTERVAL_LENGTH + MINIMUM_RESPONSE;
		PWMPin.setPwm(result);
	}
	
	public void left() {
		if (! (leftMotor.goingCounterClockwise() && rightMotor.goingClockwise())) {
			logWriter.writeToLog("Zeppelin naar links laten draaien.");
			this.leftMotor.counterClockwise();
			this.rightMotor.clockwise();
		}
	}
	
	public void right() {
		if (! (leftMotor.goingClockwise() && rightMotor.goingCounterClockwise())) {
			logWriter.writeToLog("Zeppelin naar rechts laten draaien.");
			this.rightMotor.counterClockwise();
			this.leftMotor.clockwise();
		}
	}
	
	public void stopTurning() {
		if (leftMotor.isOn() && rightMotor.isOn()) {
			logWriter.writeToLog("Zeppelin laten stoppen met draaien.");
			this.rightMotor.stop();
			this.leftMotor.stop();
		}
	}
	
	public void forward() {
		if (! (leftMotor.goingClockwise() && rightMotor.goingClockwise())) {
			logWriter.writeToLog("Zeppelin vooruit laten gaan.");
			this.leftMotor.clockwise();
			this.rightMotor.clockwise();
		}
	}
	
	public void backward() {
		if (! (leftMotor.goingCounterClockwise() && rightMotor.goingCounterClockwise())) {
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

}
