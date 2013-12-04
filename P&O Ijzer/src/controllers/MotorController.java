package controllers;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import com.pi4j.io.gpio.GpioPinPwmOutput;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.wiringpi.SoftPwm;

import zeppelin.MainProgramImpl;
import components.Motor;
import ftp.LogWriter;

public class MotorController implements Serializable {

	private static final GpioPinPwmOutput PWMPin = MainProgramImpl.gpio.provisionPwmOutputPin(RaspiPin.GPIO_01);
	private static final int MINIMUM_RESPONSE = 900;
	private static final int MAXIMUM_RESPONSE = 1024;
	private static final int INTERVAL_LENGTH = MAXIMUM_RESPONSE - MINIMUM_RESPONSE;
	
	private Motor leftMotor;
	private Motor rightMotor;
	private Motor downwardMotor;
	
	private static final int LEFT_CLOCKWISE_PIN = 11;
	private static final int LEFT_COUNTERCLOCKWISE_PIN = 13;
	private static final int RIGHT_CLOCKWISE_PIN = 4;
	private static final int RIGHT_COUNTERCLOCKWISE_PIN = 0;
	
	private int softPwmValue = 0;
	
	private static final LogWriter logWriter = new LogWriter();
	
	// Motor 1: GPIO_05 en GPIO_07
	// Motor 2: GPIO_00 en GPIO_04
	// Motor 3: GPIO_13 en GPIO_11
	// Motor 4: GPIO_12 en GPIO_14
	public MotorController() {
		leftMotor = new Motor(RaspiPin.GPIO_11, RaspiPin.GPIO_13); // motor 3
		rightMotor = new Motor(RaspiPin.GPIO_04, RaspiPin.GPIO_00); // motor 2
		downwardMotor = new Motor(RaspiPin.GPIO_14, RaspiPin.GPIO_12); // motor 4
		SoftPwm.softPwmCreate(LEFT_CLOCKWISE_PIN, 0, 100);
		SoftPwm.softPwmCreate(LEFT_COUNTERCLOCKWISE_PIN, 0, 100);
		SoftPwm.softPwmCreate(RIGHT_CLOCKWISE_PIN, 0, 100);
		SoftPwm.softPwmCreate(RIGHT_COUNTERCLOCKWISE_PIN, 0, 100);
		
	}
	
	public void setHeightSpeed(int percentage) {
		if (percentage < 0) {
			this.up();
		}
		else {
			this.down();
		}
		if (PWMPin.getPwm() != percentage) {
			if (percentage == 0) {
				PWMPin.setPwm(0);
				return;
			}
			if (Math.abs(percentage) > 100)
				percentage = 100;
			double factor = (double) Math.abs(percentage) / 100;
			int result = (int) factor * INTERVAL_LENGTH + MINIMUM_RESPONSE;
			PWMPin.setPwm(Math.abs(result));
		}
		logWriter.writeToLog("Onderwaartse motor laten draaien aan percentage: "
				+ percentage);
	}
	
	public void setTurnSpeed(int percentage) {
		if (percentage < 0) { // zeppelin moet naar rechts
			logWriter.writeToLog("Rechtse motor laten draaien aan percentage: "
					+ (- percentage));
			this.right();
		}
		else { // zeppelin moet naar links
			logWriter.writeToLog("Linkse motor laten draaien aan percentage: "
					+ percentage);
			this.left();
		}
		if (softPwmValue != percentage) {
			if (percentage == 0) {
				updateSoftPwm(percentage);
				return;
			}
			if (Math.abs(percentage) > 100)
				percentage = 100;
			this.softPwmValue = percentage;
			this.updateSoftPwm(percentage);
		}
	}
	
	private void updateSoftPwm(int percentage) {
		if (goingLeft()) {
			SoftPwm.softPwmWrite(RIGHT_CLOCKWISE_PIN, percentage);
		}
		else if (goingRight()) {
			SoftPwm.softPwmWrite(LEFT_CLOCKWISE_PIN, percentage);
		}
		else if (goingForward()) {
			SoftPwm.softPwmWrite(LEFT_CLOCKWISE_PIN, percentage);
			SoftPwm.softPwmWrite(RIGHT_CLOCKWISE_PIN, percentage);
		}
		else if (goingBackward()) {
			SoftPwm.softPwmWrite(LEFT_COUNTERCLOCKWISE_PIN, percentage);
			SoftPwm.softPwmWrite(RIGHT_COUNTERCLOCKWISE_PIN, percentage);
		}
	}
	
	public void left() {
		if (! this.goingLeft()) {
			logWriter.writeToLog("Zeppelin naar links laten draaien.");
			this.writeSoftPwmValues(0, 0, softPwmValue, 0);
			this.rightMotor.clockwise();
		}
	}
	
	public void right() {
		if (! this.goingRight()) {
			logWriter.writeToLog("Zeppelin naar rechts laten draaien.");
			this.writeSoftPwmValues(softPwmValue, 0, 0, 0);
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
		if (! this.goingForward()) {
			logWriter.writeToLog("Zeppelin vooruit laten gaan.");
			this.writeSoftPwmValues(softPwmValue, 0, softPwmValue, 0);
			this.leftMotor.clockwise();
			this.rightMotor.clockwise();
		}
	}
	
	public void backward() {
		if (! this.goingBackward()) {
			logWriter.writeToLog("Zeppelin achteruit laten gaan.");
			this.writeSoftPwmValues(0, softPwmValue, 0, softPwmValue);
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
        return this.rightMotor.goingClockwise() && (! this.leftMotor.isOn());
	}

	public boolean goingRight() {
		return this.leftMotor.goingClockwise() && (! this.rightMotor.isOn());
	}
	
	public boolean goingForward() {
		return this.leftMotor.goingClockwise() && this.rightMotor.goingClockwise();
	}
	
	public boolean goingBackward() {
		return this.leftMotor.goingCounterClockwise() && this.rightMotor.goingCounterClockwise();
	}
	
	private void writeSoftPwmValues(int leftClockwise, int leftCounterClockwise, int rightClockwise, int rightCounterClockwise) {
		SoftPwm.softPwmWrite(LEFT_CLOCKWISE_PIN, leftClockwise);
		SoftPwm.softPwmWrite(LEFT_COUNTERCLOCKWISE_PIN, leftCounterClockwise);
		SoftPwm.softPwmWrite(RIGHT_CLOCKWISE_PIN, rightClockwise);
		SoftPwm.softPwmWrite(RIGHT_COUNTERCLOCKWISE_PIN, rightCounterClockwise);
	}

}
