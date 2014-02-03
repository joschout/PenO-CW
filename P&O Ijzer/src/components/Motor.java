/**
 * Abstracte voorstelling van één enkele motor op de zeppelin.
 */

package components;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import zeppelin.MainProgramImpl;

import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;

public class Motor implements Serializable {
	
	/**
	 * Pin die aangesproken moet worden om de motor kloksgewijs te laten draaien.
	 */
	private GpioPinDigitalOutput clockwisePin;
	
	/**
	 * Pin die aangesproken moet worden om de motor tegen de klok in te laten draaien.
	 */
	private GpioPinDigitalOutput counterClockwisePin;
	
	/**
	 * Initialiseert een motor. Argumenten zijn best niet null om "interessante" dingen te voorkomen.
	 * @param clockwise
	 *        Pin die aangesproken moet worden om de motor kloksgewijs te laten draaien.
	 * @param counterClockwise
	 *        Pin die aangesproken moet worden om de motor tegen de klok in te laten draaien.
	 */
	public Motor(Pin clockwise, Pin counterClockwise) {
		this.clockwisePin =  MainProgramImpl.gpio.provisionDigitalOutputPin(clockwise, "", PinState.LOW);
		this.counterClockwisePin = MainProgramImpl.gpio.provisionDigitalOutputPin(counterClockwise, PinState.LOW);
	}
	
	/**
	 * Laat de motor kloksgewijs draaien.
	 */
	public void clockwise() {
		if (this.counterClockwisePin.isHigh())
			this.counterClockwisePin.low();
		this.clockwisePin.high();
	}
	
	/**
	 * Laat de motor tegen de klok in draaien.
	 */
	public void counterClockwise() {
		if (this.clockwisePin.isHigh())
			this.clockwisePin.low();
		this.counterClockwisePin.high();
	}
	
	/**
	 * Zet de motor af.
	 */
	public void stop() {
		if (this.clockwisePin.isHigh()) 
			this.clockwisePin.low();
		if (this.counterClockwisePin.isHigh()) 
			this.counterClockwisePin.low();
	}
	
	/**
	 * Geeft aan of de motor aanstaat.
	 */
	public boolean isOn() {
		return (this.clockwisePin.isHigh() || this.counterClockwisePin.isHigh());
	}
	
	/**
	 * Geeft aan of de motor kloksgewijs aan het draaien is.
	 */
	public boolean goingClockwise() {
		return this.clockwisePin.isHigh();
	}
	
	/**
	 * Geeft aan of de motor tegen de klok in aan het draaien is.
	 */
	public boolean goingCounterClockwise() {
		return this.counterClockwisePin.isHigh();
	}

}
