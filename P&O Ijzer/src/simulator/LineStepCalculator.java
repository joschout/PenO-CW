package simulator;

import java.util.ArrayList;
import java.util.List;

import coordinate.GridPoint;

public class LineStepCalculator {
	
	/**
	 * Beweeg step cm vanaf currentPosition naar goal
	 * 
	 * @param currentPosition
	 * 		Huidige positie
	 * @param goal
	 * 		Doelpositie
	 * @param step
	 * 		Aantal cm om te bewegen
	 * @return Nieuwe positie, die step cm verwijderd is van currentPosition
	 * 		en step cm dichter bij goal
	 */
	public GridPoint moveTowards(GridPoint currentPosition, GridPoint goal, double step) {
		if (currentPosition.distanceTo(goal) < step) {
			return goal;
		}
		
		Line line = new Line(currentPosition, goal);
		
		if (line.isVertical()) {
			return this.handleVerticalLine(currentPosition, goal, step);
		}
		
		double a = this.makeQuadraticTerm(line);
		double b = this.makeLinearTerm(currentPosition, line, step);
		double c = this.makeConstantTerm(currentPosition, line, step);
		QuadraticSolver solver = new QuadraticSolver(a, b, c);
		
		List<Double> candidates = solver.getSolutions();
		GridPoint toReturn = null;
		double distanceToGoal = Double.POSITIVE_INFINITY;
		for (double candidate : candidates) {
			GridPoint candidatePoint = new GridPoint(candidate, line.evaluate(candidate));
			double candidateDistance = candidatePoint.distanceTo(goal);
			if (candidateDistance < distanceToGoal) {
				toReturn = candidatePoint;
				distanceToGoal = candidateDistance;
			}
		}
		return toReturn;
	}
	
	private double makeQuadraticTerm(Line line) {
		return 1 + Math.pow(line.getSlope(), 2);
	}
	
	private double makeLinearTerm(GridPoint currentPosition, Line line, double step) {
		return -2 * currentPosition.x + 2 * line.getSlope() * line.getIntercept()
				- 2 * line.getSlope() * currentPosition.y;
	}
	
	private double makeConstantTerm(GridPoint currentPosition, Line line, double step) {
		return - Math.pow(step, 2) + Math.pow(currentPosition.x, 2) + Math.pow(line.getIntercept(), 2)
				- 2 * line.getIntercept() * currentPosition.y + Math.pow(currentPosition.y, 2);
	}
	
	private GridPoint handleVerticalLine(GridPoint currentPosition, GridPoint goal, double step) {
		List<GridPoint> candidates = new ArrayList<GridPoint>();
		candidates.add(new GridPoint(currentPosition.x, currentPosition.y + step));
		candidates.add(new GridPoint(currentPosition.x, currentPosition.y - step));GridPoint toReturn = null;
		double distanceToGoal = Double.POSITIVE_INFINITY;
		for (GridPoint candidatePoint : candidates) {
			double candidateDistance = candidatePoint.distanceTo(goal);
			if (candidateDistance < distanceToGoal) {
				toReturn = candidatePoint;
				distanceToGoal = candidateDistance;
			}
		}
		return toReturn;
	}

}
