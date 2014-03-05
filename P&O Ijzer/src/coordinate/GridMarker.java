package coordinate;

public class GridMarker {
	
	public GridMarker(String colour, String shape, Point point)
	{
		this.shape = shape;
		this.colour = colour;
		this.point = point;
	}
	
	public GridMarker(String colour, String shape)
	{
		this(colour, shape, null);
	}
	
	public GridMarker()
	{
		this(null, null, null);
	}
	
	private String shape;
	
	public String getShape()
	{
		return shape;
	}
	
	public void setShape(String shape)
	{
		this.shape = shape;
	}
	
	private String colour;
	
	public String getColour()
	{
		return colour;
	}
	
	public void setColour(String colour)
	{
		this.colour = colour;
	}
	
	private Point point;
	
	public Point getPoint()
	{
		return point;
	}
	
	public void setPoint(Point point)
	{
		this.point = point;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((colour == null) ? 0 : colour.hashCode());
		result = prime * result + ((shape == null) ? 0 : shape.hashCode());
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
		GridMarker other = (GridMarker) obj;
		if (colour == null) {
			if (other.colour != null)
				return false;
		} else if (!colour.equals(other.colour))
			return false;
		if (shape == null) {
			if (other.shape != null)
				return false;
		} else if (!shape.equals(other.shape))
			return false;
		return true;
	}
	
	@Override
	public String toString()
	{
		return colour + " " + shape + " " + point.toString();
	}
}
