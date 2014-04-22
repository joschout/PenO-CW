package simulator;

import coordinate.GridPoint;

public class PositionStepper {
	
	public PositionStepper() {
		this.calc = new LineStepCalculator();
	}
	
	public GridPoint moveTowards(GridPoint position, GridPoint targetPosition, double step) {
		return this.getLineStepCalculator().moveTowards(position, targetPosition, step);
	}
	
	private LineStepCalculator calc;
	
	private LineStepCalculator getLineStepCalculator() {
		return this.calc;
	}

}
