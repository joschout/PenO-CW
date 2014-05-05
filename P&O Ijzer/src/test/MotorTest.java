package test;

import java.io.IOException;
import java.util.Scanner;

import movement.HeightController;

import com.pi4j.io.gpio.RaspiPin;

import controllers.MotorController;
import controllers.SensorController;
import controllers.SensorController.TimeoutException;

public class MotorTest {
	
	
	public static void main(String[] args) throws IOException, InterruptedException {
		
		int percentage = Integer.parseInt(args[0]);
		
		MotorController motorController = new MotorController();
		SensorController sensorController = new SensorController(RaspiPin.GPIO_03, RaspiPin.GPIO_06);
		HeightController heightAdjuster = new HeightController(sensorController,motorController);
		
		System.out.println("Druk op q om af te sluiten.");
		boolean exit = false;
		Scanner in = new Scanner(System.in);
		motorController.forward(percentage);
		while (! exit) {
			try {
				heightAdjuster.goToHeight(100);
			} catch (TimeoutException e) {
				e.printStackTrace();
			}
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
		in.close();
	}

}
