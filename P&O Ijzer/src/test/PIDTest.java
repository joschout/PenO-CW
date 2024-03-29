package test;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Scanner;
import com.pi4j.io.gpio.RaspiPin;

import controllers.MotorController;
import controllers.SensorController;
import controllers.SensorController.TimeoutException;
import movement.HeightController;
import movement.PIDController;


public class PIDTest {

	/**
	 * @param args
	 * @throws InterruptedException 
	 * @throws TimeoutException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws TimeoutException, InterruptedException, IOException {
		// args = new String[10];

		int percentage = Integer.parseInt(args[0]);
		
		MotorController motorController = new MotorController();
//		SensorController sensorController = new SensorController(RaspiPin.GPIO_03, RaspiPin.GPIO_06);
//		HeightController heightAdjuster = new HeightController(sensorController,motorController);

//		PIDController pid = heightAdjuster.getpController();

//		pid.setKd(0);
//		pid.setKi(0);
//		pid.setKp(0.5);


		System.out.println("Druk op q om af te sluiten.");
		boolean exit = false;
		Scanner in = new Scanner(System.in);
		motorController.setHeightSpeed(percentage);
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
		in.close();
	}

}
