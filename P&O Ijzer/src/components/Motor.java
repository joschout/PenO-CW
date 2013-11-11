package components;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import zeppelin.Zeppelin;

import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;

public class Motor implements Serializable {
	
	private GpioPinDigitalOutput clockwisePin;
	private GpioPinDigitalOutput counterClockwisePin;
	
	public Motor(Pin clockwise, Pin counterClockwise) {
		this.clockwisePin =  Zeppelin.gpio.provisionDigitalOutputPin(clockwise);
		this.counterClockwisePin = Zeppelin.gpio.provisionDigitalOutputPin(counterClockwise);
	}
	
	public void clockwise() {
		if (this.counterClockwisePin.isHigh())
			this.counterClockwisePin.low();
		this.clockwisePin.high();
	}
	
	public void counterClockwise() {
		if (this.clockwisePin.isHigh())
			this.clockwisePin.low();
		this.counterClockwisePin.high();
	}
	
	public void stop() {
		if (this.clockwisePin.isHigh()) 
			this.clockwisePin.low();
		if (this.counterClockwisePin.isHigh()) 
			this.counterClockwisePin.low();
	}
	
	public boolean isOn() {
		return (this.clockwisePin.isHigh() || this.counterClockwisePin.isHigh());
	}
	
	public boolean goingClockwise() {
		return this.clockwisePin.isHigh();
	}
	
	public boolean goingCounterClockwise() {
		return this.counterClockwisePin.isHigh();
	}

}
