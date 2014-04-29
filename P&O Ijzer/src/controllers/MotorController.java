/**
 * Laat toe om de zeppelin horizontaal en verticaal te laten bewegen.
 */

package controllers;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;


import logger.LogWriter;


import com.pi4j.io.gpio.GpioPinPwmOutput;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.wiringpi.SoftPwm;

import zeppelin.MainProgramImpl;
import components.Motor;

public class MotorController implements Serializable {

	/**
	 * De enige pin van de Raspberry Pi die PWM ondersteunt.
	 */
	private static final GpioPinPwmOutput PWMPin = MainProgramImpl.gpio.provisionPwmOutputPin(RaspiPin.GPIO_01);
	/**
	 * De minimale PWM-waarde waarvoor er merkbare respons is.
	 */
	private static final int MINIMUM_RESPONSE = 900;
	/**
	 * De maximale PWM-waarde waarvoor er respons is.
	 */
	private static final int MAXIMUM_RESPONSE = 1024;
	private static final int INTERVAL_LENGTH = MAXIMUM_RESPONSE - MINIMUM_RESPONSE;
	
	private Motor leftMotor;
	private Motor rightMotor;
	private Motor downwardMotor;
	
	private static final int LEFT_CLOCKWISE_PIN = 11;
	private static final int LEFT_COUNTERCLOCKWISE_PIN = 13;
	private static final int RIGHT_CLOCKWISE_PIN = 4;
	private static final int RIGHT_COUNTERCLOCKWISE_PIN = 0;
	
	/**
	 * Waarde tussen 0 en 100 om software PWM voor de horizontale motoren te regelen.
	 */
	private int softPwmValue = 0;
	
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
	
	/**
	 * Zet de PWM-waarde voor de hoogte op het gegeven percentage.
	 */
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

		LogWriter.INSTANCE.writeToLog("Onderwaartse motor laten draaien aan percentage: "
				+ percentage);
	}
	
	/**
	 * Zet de PWM-waarde voor de hoek op het gegeven percentage.
	 * @param percentage
	 */
	public void setTurnSpeed(int percentage) {
		if (Math.abs(percentage) > 50) {
			percentage = (int) Math.signum((double) percentage) * 50;
		}
		System.out.println("Laten draaien aan percentage: " + percentage);
		if (percentage < 0) { // zeppelin moet naar rechts
			LogWriter.INSTANCE.writeToLog("Linkse motor laten draaien aan percentage: "

					+ (- percentage));
			this.right();
		}
		else { // zeppelin moet naar links

			LogWriter.INSTANCE.writeToLog("Rechtse motor laten draaien aan percentage: "

					+ percentage);
			this.left();
		}
		if (softPwmValue != Math.abs(percentage)) {
			if (Math.abs(percentage) > 50)
				percentage = 50;
			this.softPwmValue = percentage;
			this.updateSoftPwm(percentage);
		}
	}
	
	/**
	 * Zet de software PWM waarde voor de juiste pinnen voor de juiste motoren.
	 */
	private void updateSoftPwm(int percentage) {
		percentage = Math.abs(percentage);
		if (goingLeft()) {
			SoftPwm.softPwmWrite(LEFT_COUNTERCLOCKWISE_PIN, percentage);
			SoftPwm.softPwmWrite(RIGHT_CLOCKWISE_PIN, percentage);
		}
		else if (goingRight()) {
			SoftPwm.softPwmWrite(LEFT_CLOCKWISE_PIN, percentage);
			SoftPwm.softPwmWrite(RIGHT_COUNTERCLOCKWISE_PIN, percentage);
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
	
	/**
	 * Laat de zeppelin naar links draaien en hevelt de software PWM waarde over naar de juiste pinnen.
	 */
	public void left() {
		if (! this.goingLeft()) {

			LogWriter.INSTANCE.writeToLog("Zeppelin naar links laten draaien.");

		}
		this.writeSoftPwmValues(0, softPwmValue, softPwmValue, 0);
		this.leftMotor.counterClockwise();
		this.rightMotor.clockwise();
	}
	
	/**
	 * Laat de zeppelin naar rechts draaien en hevelt de software PWM waarde over naar de juiste pinnen.
	 */
	public void right() {
		if (! this.goingRight()) {

			LogWriter.INSTANCE.writeToLog("Zeppelin naar rechts laten draaien.");

		}
		this.writeSoftPwmValues(softPwmValue, 0, 0, softPwmValue);
		this.leftMotor.clockwise();
		this.rightMotor.counterClockwise();
	}
	
	/**
	 * Laat de zeppelin naar links draaien. Aangezien de client de bron is,
	 * laat de motoren draaien op volle kracht.
	 */
	public void clientLeft() {
		if (! this.goingLeft()) {

			LogWriter.INSTANCE.writeToLog("Zeppelin naar links laten draaien.");

			this.writeSoftPwmValues(0, 100, 100, 0);
			this.leftMotor.counterClockwise();
			this.rightMotor.clockwise();
		}
	}
	
	/**
	 * Laat de zeppelin naar rechts draaien. Aangezien de client de bron is,
	 * laat de motoren draaien op volle kracht.
	 */
	public void clientRight() {
		if (! this.goingLeft()) {

			LogWriter.INSTANCE.writeToLog("Zeppelin naar rechts laten draaien.");

			this.writeSoftPwmValues(100, 0, 0, 100);
			this.leftMotor.clockwise();
			this.rightMotor.counterClockwise();
		}
	}

	/**
	 * Zet de linker- en rechtermotor af.
	 */
	public void stopRightAndLeftMotor() {
		if (leftMotor.isOn() && rightMotor.isOn()) {

			LogWriter.INSTANCE.writeToLog("Zeppelin de linker- en rechtermotor laten stoppen.");

		}
		this.writeSoftPwmValues(0, 0, 0, 0);
		this.rightMotor.stop();
		this.leftMotor.stop();
	}
	
	/**
	 * Laat de zeppelin vooruit gaan. Deze methode wordt opgeroepen door
	 * ForwardBackwardController; daarom worden software PWM waardes van 30 geschreven
	 * in plaats van 100.
	 */
	public void forward() {
		if (! this.goingForward()) {

			LogWriter.INSTANCE.writeToLog("Zeppelin vooruit laten gaan.");

		}
		this.writeSoftPwmValues(30, 0, 30, 0);
		this.leftMotor.clockwise();
		this.rightMotor.clockwise();
	}
	
	/**
	 * Laat de zeppelin achteruit gaan. Deze methode wordt opgeroepen door
	 * ForwardBackwardController; daarom worden software PWM waardes van 30 geschreven
	 * in plaats van 100.
	 */
	public void backward() {
		if (! this.goingBackward()) {

			LogWriter.INSTANCE.writeToLog("Zeppelin achteruit laten gaan.");

		}
		this.writeSoftPwmValues(0, 30, 0, 30);
		this.leftMotor.counterClockwise();
		this.rightMotor.counterClockwise();
	}
	
	/**
	 * Laat de zeppelin stijgen.
	 */
	public void up() {
		if (! downwardMotor.goingClockwise()) {

			LogWriter.INSTANCE.writeToLog("Zeppelin laten stijgen.");

			this.downwardMotor.clockwise();
		}
	}
	
	/**
	 * Laat de zeppelin dalen.
	 */
	public void down() {
		if (! downwardMotor.goingCounterClockwise()) {

			LogWriter.INSTANCE.writeToLog("Zeppelin laten dalen.");

			this.downwardMotor.counterClockwise();
		}
	}
	
	/**
	 * Laat de zeppelin de naar-beneden-gerichte motor afzetten.
	 */
	public void stopHeightAdjustment() {
		if (downwardMotor.isOn()) {

			LogWriter.INSTANCE.writeToLog("Zeppelin laten stoppen met zijn hoogte aan te passen.");

			this.downwardMotor.stop();
		}
	}
	
	/**
	 * Deprecated.
	 */
	public ArrayList<Motor> getMotors() {
		ArrayList<Motor> toReturn = new ArrayList<Motor>();
		toReturn.add(leftMotor);
		toReturn.add(rightMotor);
		toReturn.add(downwardMotor);
		return toReturn;
	}
	
	/**
	 * Geeft aan of de linkermotor aanstaat.
	 */
	public boolean leftIsOn() {
		return this.leftMotor.isOn();
	}
	
	/**
	 * Geeft aan of de rechtermotor aanstaat.
	 */
	public boolean rightIsOn() {
		return this.rightMotor.isOn();
	}
	
	/**
	 * Geeft aan of de naar-beneden-gerichte motor aanstaat.
	 */
	public boolean downwardIsOn() {
		return this.downwardMotor.isOn();
	}
	
	/**
	 * Schrijft de gegeven software PWM values weg voor de respectievelijke pinnen.
	 * @param leftClockwise
	 *        Software PWM waarde die geschreven moet worden voor de kloksgewijze pin van de linkermotor.
	 * @param leftCounterClockwise
	 *        Software PWM waarde die geschreven moet worden voor de tegenkloksgewijze pin van de linkermotor.
	 * @param rightClockwise
	 *        Software PWM waarde die geschreven moet worden voor de kloksgewijze pin van de rechtermotor.
	 * @param rightCounterClockwise
	 *        Software PWM waarde die geschreven moet worden voor de tegenkloksgewijze pin van de rechtermotor.
	 */
	public void writeSoftPwmValues(int leftClockwise, int leftCounterClockwise, int rightClockwise, int rightCounterClockwise) {
		SoftPwm.softPwmWrite(LEFT_CLOCKWISE_PIN, leftClockwise);
		SoftPwm.softPwmWrite(LEFT_COUNTERCLOCKWISE_PIN, leftCounterClockwise);
		SoftPwm.softPwmWrite(RIGHT_CLOCKWISE_PIN, rightClockwise);
		SoftPwm.softPwmWrite(RIGHT_COUNTERCLOCKWISE_PIN, rightCounterClockwise);
	}
	
	/**
	 * Geeft aan of de zeppelin vooruit aan het gaan is.
	 */
	public boolean goingForward() {
		return this.leftMotor.goingClockwise() && this.rightMotor.goingClockwise();
	}
	
	/**
	 * Geeft aan of de zeppelin achteruit aan het gaan is.
	 */
	public boolean goingBackward() {
		return this.leftMotor.goingCounterClockwise() && this.rightMotor.goingCounterClockwise();
	}
	
	/**
	 * Geeft aan of de zeppelin naar links aan het draaien is.
	 */
	public boolean goingLeft() {
		return this.leftMotor.goingCounterClockwise() && this.rightMotor.goingClockwise();
	}
	
	/**
	 * Geeft aan of de zeppelin naar rechts aan het draaien is.
	 */
	public boolean goingRight() {
		return this.leftMotor.goingClockwise() && this.rightMotor.goingCounterClockwise();
	}
	
	/**
	 * Geeft aan of de zeppelin zich op horizontaal vlak aan het bewegen is.
	 */
	public boolean movingHorizontally() {
		return this.goingForward() || this.goingBackward() || this.goingLeft() || this.goingRight();
	}

	
}
