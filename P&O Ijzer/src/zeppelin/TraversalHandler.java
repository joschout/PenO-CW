package zeppelin;

//import java.rmi.RemoteException;

import movement.RotationController;
import controllers.MotorController;
import controllers.SensorController.TimeoutException;
import coordinate.GridPoint;

public class TraversalHandler {
	
	private MainProgramImpl zeppelin;
	private double acceptableDistance = 5;
	
	public TraversalHandler(MainProgramImpl zeppelin) {
		this.zeppelin = zeppelin;
	}
	
	public boolean moveTowardsPoint() throws  InterruptedException, TimeoutException {
		GridPoint currentPosition = this.getZeppelin().getPosition();
		GridPoint targetPosition = this.getZeppelin().getTargetPosition();
		double angle = this.calculateRotation(currentPosition, targetPosition);
		this.getZeppelin().setAngle(angle);
		
		if (! (currentPosition.distanceTo(targetPosition) < acceptableDistance))
		{
			this.getZeppelin().stopRightAndLeft();
			return false;
		}
		if (! this.getZeppelin().angleInAcceptableRange(this.getZeppelin().getMostRecentAngle()))
		{
			this.getZeppelin().moveTowardsTargetAngle();
			return true;
		}
		else
		{
			this.moveForward();
			return true;
		}
		
	}
	
	public void goToPoint(GridPoint current, GridPoint goal) throws  TimeoutException, InterruptedException {
		double angle = calculateRotation(current, goal);
		turn(angle);
		moveForward();
	}
	
	public MainProgramImpl getZeppelin() {
		return this.zeppelin;
	}

	private void moveForward() {
		this.zeppelin.goForward();
	}

	private void turn(double angle) throws TimeoutException, InterruptedException {
		GridPoint currentPosition = this.zeppelin.getPosition();
		GridPoint targetPosition = this.zeppelin.getTargetPosition();
		double targetAngle = this.calculateRotation(currentPosition, targetPosition);
		this.zeppelin.setTargetAngle(targetAngle);
		this.zeppelin.moveTowardsTargetAngle();
	}

	private double calculateRotation(GridPoint current, GridPoint goal) {
		GridPoint vector = new GridPoint(goal.x - current.x, goal.y - current.y);
		
		double angle = Math.toDegrees(Math.atan2(vector.y, vector.x));
		return angle;
		
	}
}
