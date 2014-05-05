package test;

import coordinate.GridPoint;

public class RotationTest {

	public static void main(String[] args) {
		GridPoint goal = new GridPoint(100, 100);
		GridPoint previous = new GridPoint(0, 0);
		GridPoint current = new GridPoint(0, 10);
		
		System.out.println(calculateRotation(previous, current, goal));
	}
	
	private static  double calculateRotation(GridPoint previous, GridPoint current, GridPoint goal) {
		boolean goLeft = decideLeft(previous, current, goal);
		System.out.println(goLeft);
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

	private static boolean decideLeft(GridPoint previous, GridPoint current, GridPoint goal) {
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
