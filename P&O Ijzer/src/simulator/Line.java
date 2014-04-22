package simulator;

import coordinate.GridPoint;

public class Line {
	
	public Line(double slope, double intercept) throws IllegalArgumentException {
		this.slope = slope;
		this.intercept = intercept;
	}
	
	public Line(GridPoint one, GridPoint other) {
		this((one.y - other.y) / (one.x - other.x),
				- (one.y - other.y) / (one.x - other.x) * one.x + one.y);
	}
	
	private final double slope;
	
	public double getSlope() {
		return this.slope;
	}
	
	private final double intercept;
	
	public double getIntercept() {
		return this.intercept;
	}
	
	public boolean isVertical() {
		return this.getSlope() == Double.POSITIVE_INFINITY ||
				this.getSlope() == Double.NEGATIVE_INFINITY;
	}
	
	public double evaluate(double x) throws IllegalStateException {
		if (this.isVertical()) {
			System.out.println("Evaluation is ambiguous for vertical lines.");
		}
		return this.getSlope() * x + this.getIntercept();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(intercept);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(slope);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Line other = (Line) obj;
		if (Double.doubleToLongBits(intercept) != Double
				.doubleToLongBits(other.intercept))
			return false;
		if (Double.doubleToLongBits(slope) != Double
				.doubleToLongBits(other.slope))
			return false;
		return true;
	}

}
