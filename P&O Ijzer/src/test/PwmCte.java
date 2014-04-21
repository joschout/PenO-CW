package test;

import java.io.IOException;
import java.util.Scanner;

import com.pi4j.io.gpio.RaspiPin;

import controllers.MotorController;
import controllers.SensorController;
import movement.HeightController;

public class PwmCte {

	private static int pwmValue = 10;
	
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		MotorController motorController = new MotorController();
		SensorController sensorController = new SensorController(RaspiPin.GPIO_03, RaspiPin.GPIO_06);
		
		motorController.setHeightSpeed(pwmValue);
		
		System.out.println("Druk op q om af te sluiten.");
		boolean exit = false;
		Scanner in = new Scanner(System.in);
		while (! exit) {
			if(System.in.available() > 0) {
				byte[] array = new byte[1];
				System.in.read(array, 0, 1);
				String input = new String(array);
				if (input.equals("q")) {
					exit = true;
					motorController.stopHeightAdjustment();
				}
			}
		}
	}

}
