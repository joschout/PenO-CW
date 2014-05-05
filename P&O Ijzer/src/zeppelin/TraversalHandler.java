package zeppelin;

import controllers.SensorController.TimeoutException;
import coordinate.GridPoint;

public class TraversalHandler {
	
	private MainProgramImpl zeppelin;
	private double acceptableDistance = 20;
	
	public TraversalHandler(MainProgramImpl zeppelin) {
		this.zeppelin = zeppelin;
	}
	
	public boolean moveTowardsPoint() throws  InterruptedException, TimeoutException {
		GridPoint previousPosition = this.getZeppelin().getPreviousPosition();
		GridPoint currentPosition = this.getZeppelin().getPosition();
		GridPoint targetPosition = this.getZeppelin().getTargetPosition();
		double angle = this.calculateRotation(previousPosition, currentPosition, targetPosition);
		this.getZeppelin().setAngleError(angle);
		
		if (! (currentPosition.distanceTo(targetPosition) < acceptableDistance))
		{
			this.getZeppelin().stopRightAndLeft();
			return false;
		}
		if (! this.getZeppelin().angleInAcceptableRange(this.getZeppelin().getAngleError()))
		{
			this.getZeppelin().moveTowardsTargetAngle();
		}
		Thread.sleep(200);
		this.moveForward();
		Thread.sleep(200);
		return true;
		
	}
	
	public void goToPoint(GridPoint previous, GridPoint current, GridPoint goal) throws  TimeoutException, InterruptedException {
		double angle = calculateRotation(previous, current, goal);
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
		GridPoint previousPosition = this.zeppelin.getPreviousPosition();
		GridPoint currentPosition = this.zeppelin.getPosition();
		GridPoint targetPosition = this.zeppelin.getTargetPosition();
		double targetAngle = this.calculateRotation(previousPosition, currentPosition, targetPosition);
		//TODO NIEUWE IMPLEMENTATIE HIER?
		this.zeppelin.setTargetAngle(targetAngle);
		this.zeppelin.moveTowardsTargetAngle();
	}

	private double calculateRotation(GridPoint previous, GridPoint current, GridPoint goal) {
		Boolean goLeft = decideLeft(previous, current, goal);
		GridPoint vectorGoal = new GridPoint(goal.x - previous.x, goal.y - previous.y);
		GridPoint vectorCurrent = new GridPoint(current.x - previous.x, current.y - previous.y);
		
		double sum = vectorGoal.x * vectorCurrent.x + vectorGoal.y * vectorCurrent.y;
		double divide1 = Math.sqrt(Math.pow(vectorGoal.x, 2) + Math.pow(vectorGoal.y, 2));
		double divide2 = Math.sqrt(Math.pow(vectorCurrent.x, 2) + Math.pow(vectorCurrent.y, 2));
		
		double angle = Math.acos(sum/(divide1*divide2));
		if(! goLeft) {
			angle = - angle;
		}
		return Math.toDegrees(angle);
		
	}

	private boolean decideLeft(GridPoint previous, GridPoint current, GridPoint goal) {
		GridPoint vectorGoal = new GridPoint(goal.x - previous.x, goal.y - previous.y);
		GridPoint vectorCurrent = new GridPoint(current.x - previous.x, current.y - previous.y);
		
		double cross = vectorGoal.x * vectorCurrent.y - vectorGoal.y * vectorCurrent.x;
		if(cross < 0) {
			return false;
		}
		else {
			return true;
		}
		
	}
}
