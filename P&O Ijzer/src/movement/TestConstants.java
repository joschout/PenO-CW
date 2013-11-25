package movement;
import java.rmi.RemoteException;
import java.util.*;

import controllers.MotorController;
import controllers.SensorController.TimeoutException;
public class TestConstants {

	
	
	public static void main(String[] args) throws RemoteException, InterruptedException {
		
		MotorController motorController = new MotorController();
		HeightAdjuster heightAdjuster = new HeightAdjuster(motorController);
		
		Random randHeightGen = new Random();
		double randHeight = randHeightGen.nextDouble();
		randHeight= randHeight*300;
		double targetHeight = 150;
		
		int i =0;
		while (i<=20)
			try {				
				heightAdjuster.takeAction(randHeight, targetHeight);
			} catch (TimeoutException e) {
				e.printStackTrace();
			}
			try {
				Thread.sleep(500);
				i++;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		
	}

	
	
	
	
	
