package test;

import com.pi4j.io.gpio.RaspiPin;

import controllers.MotorController;
import controllers.SensorController;
import movement.HeightController;

public class PIDTest {

	private static int pwmValue = 10;
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MotorController motorController = new MotorController();
		SensorController sensorController = new SensorController(RaspiPin.GPIO_03, RaspiPin.GPIO_06);
		
		motorController.setHeightSpeed(pwmValue);
	}

}
