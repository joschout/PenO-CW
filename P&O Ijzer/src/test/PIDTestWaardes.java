package test;

import java.io.IOException;
import java.util.Scanner;

import movement.HeightController;
import movement.PIDController;

import com.pi4j.io.gpio.RaspiPin;

import controllers.MotorController;
import controllers.SensorController;
import controllers.SensorController.TimeoutException;

public class PIDTestWaardes {

	public static void main(String[] args) throws TimeoutException, InterruptedException, IOException {
		// args = new String[10];

		double Kp = Double.parseDouble(args[0]);
		double Kd = Double.parseDouble(args[1]);
		double Ki = Double.parseDouble(args[2]);
		
		MotorController motorController = new MotorController();
		SensorController sensorController = new SensorController(RaspiPin.GPIO_03, RaspiPin.GPIO_06);
		HeightController heightAdjuster = new HeightController(sensorController,motorController);

		PIDController pid = heightAdjuster.getpController();

		pid.setKp(Kp);
		pid.setKd(Kd);
		pid.setKi(Ki);


		System.out.println("Druk op q om af te sluiten.");
		boolean exit = false;
		Scanner in = new Scanner(System.in);
		while (! exit) {
			try {
				heightAdjuster.goToHeight(100);
				if(System.in.available() > 0) {
					byte[] array = new byte[1];
					System.in.read(array, 0, 1);
					String input = new String(array);
					if (input.equals("q")) {
						exit = true;
						motorController.stopHeightAdjustment();
					}
				}
			} catch (TimeoutException e) {
				e.printStackTrace();
			}
			


		}
	}
	
}
