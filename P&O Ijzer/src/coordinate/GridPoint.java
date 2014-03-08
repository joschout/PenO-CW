package coordinate;

public class GridPoint {
	
	/**
	 * Horizontale coördinaat van het punt, uitgedrukt in cm.
	 */
	public final double x;
	/**
	 * Verticale coördinaat van het punt, uitgedrukt in cm.
	 */
	public final double y;
	
	public GridPoint(double x, double y)
	{
		this.x = x;
		this.y = y;
	}
	
	public GridPoint add(double x, double y)
	{
		return new GridPoint(this.x + x, this.y + y);
	}
	
	public GridPoint multiply(double scalar)
	{
		return new GridPoint(this.x * scalar, this.y * scalar);
	}
	
	public double distanceTo(GridPoint other)
	{
		double xComponent = Math.pow(this.x - other.x, 2);
		double yComponent = Math.pow(this.y - other.y, 2);
		return Math.sqrt(xComponent + yComponent);
	}
	
	public GridPoint findMidpointBetween(GridPoint other)
	{
		double xComponent = (this.x + other.x) / 2d;
		double yComponent = (this.y + other.y) / 2d;
		return new GridPoint(xComponent, yComponent);
	}
	
	public GridPoint copyOf()
	{
		return new GridPoint(this.x, this.y);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(x);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(y);
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
		GridPoint other = (GridPoint) obj;
		if (Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x))
			return false;
		if (Double.doubleToLongBits(y) != Double.doubleToLongBits(other.y))
			return false;
		return true;
	}
	
	

}
