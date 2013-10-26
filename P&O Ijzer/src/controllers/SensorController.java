/**
 * Veel van deze code is gekopieerd van http://goo.gl/dpOsxb
 * 
 * De sensorcontroller staat in voor het bepalen van de afstand tussen de sensor en de grond.
 */

package controllers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;

public class SensorController implements Serializable {
	
	private final static float SOUND_SPEED = 340.29f;  // speed of sound in m/s
    
    private final static int TRIG_DURATION_IN_MICROS = 10; // trigger duration of 10 micro s
    private final static int WAIT_DURATION_IN_MILLIS = 60; // wait 60 milli s

    private final static int TIMEOUT = 2100;
    
    private final static GpioController gpio = GpioFactory.getInstance();
    
    private final GpioPinDigitalInput echoPin;
    private final GpioPinDigitalOutput trigPin;
    
    public SensorController( Pin echoPin, Pin trigPin ) {
        this.echoPin = gpio.provisionDigitalInputPin( echoPin );
        this.trigPin = gpio.provisionDigitalOutputPin( trigPin );
        this.trigPin.low();
    }
    
    /**
     * This method returns the distance measured by the sensor in cm
     * 
     * @throws TimeoutException if a timeout occurs
     */
    public float measureDistance() throws TimeoutException {
        this.triggerSensor();
        this.waitForSignal();
        long duration = this.measureSignal();
        
        return duration * SOUND_SPEED / ( 2 * 10000 );
    }
    
    /**
     * Meet honderd keer de afstand tussen de sensor en de grond en neemt van de verzameling metingen de mediaan.
     * @return De mediaan van honderd metingen
     * @throws TimeoutException
     */
    public float sensorReading() throws TimeoutException {
    	List<Float> readings = new ArrayList<Float>();
    	for (int i = 0; i < 99; i++)
    	{
    		readings.add(this.measureDistance());
    	}
    	Collections.sort(readings);
    	return (readings.get(49) + readings.get(50)) / 2; // mediaan voor een even aantal elementen
    }
    
    /**
     * Put a high on the trig pin for TRIG_DURATION_IN_MICROS
     */
    private void triggerSensor() {
        try {
            this.trigPin.high();
            Thread.sleep( 0, TRIG_DURATION_IN_MICROS * 1000 );
            this.trigPin.low();
        } catch (InterruptedException ex) {
            System.err.println( "Interrupt during trigger" );
        }
    }
    
    /**
     * Wait for a high on the echo pin
     * 
     * @throws DistanceMonitor.TimeoutException if no high appears in time
     */
    private void waitForSignal() throws TimeoutException {
        int countdown = TIMEOUT;
        
        while( this.echoPin.isLow() && countdown > 0 ) {
            countdown--;
        }
        
        if( countdown <= 0 ) {
            throw new TimeoutException( "Timeout waiting for signal start" );
        }
    }
    
    /**
     * @return the duration of the signal in micro seconds
     * @throws DistanceMonitor.TimeoutException if no low appears in time
     */
    private long measureSignal() throws TimeoutException {
        int countdown = TIMEOUT;
        long start = System.nanoTime();
        while( this.echoPin.isHigh() && countdown > 0 ) {
            countdown--;
        }
        long end = System.nanoTime();
        
        if( countdown <= 0 ) {
            throw new TimeoutException( "Timeout waiting for signal end" );
        }
        
        return (long)Math.ceil( ( end - start ) / 1000.0 );  // Return micro seconds
    }
    
    /**
     * Exception thrown when timeout occurs
     */
    public static class TimeoutException extends Exception {

        private final String reason;
        
        public TimeoutException( String reason ) {
            this.reason = reason;
        }
        
        @Override
        public String toString() {
            return this.reason;
        }
    }
}
