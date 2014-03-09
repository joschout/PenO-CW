package positioning;

import java.rmi.RemoteException;

import movement.RotationController;
import controllers.MotorController;
import controllers.SensorController.TimeoutException;
import coordinate.GridPoint;

public class Travel {
	
	private MotorController motorController;
	private RotationController rotationController;
	
	public Travel(MotorController motorController, RotationController rotationController) {
		this.motorController = motorController;
		this.rotationController = rotationController;
	}
	
	public void goToPoint(GridPoint current, GridPoint goal) throws RemoteException, TimeoutException, InterruptedException {
		double angle = calculateRotation(current, goal);
		turn(angle);
		moveForward();
	}

	private void moveForward() {
		motorController.forward();
		
	}

	private void turn(double angle) throws RemoteException, TimeoutException, InterruptedException {
		rotationController.goToAngle(angle);
		
	}

	private double calculateRotation(GridPoint current, GridPoint goal) {
		GridPoint vector = new GridPoint(goal.x - current.x, goal.y - current.y);
		
		double angle = Math.toDegrees(Math.atan2(vector.y, vector.x));
		return angle;
		
	}
}
