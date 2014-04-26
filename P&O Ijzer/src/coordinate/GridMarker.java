package coordinate;

import java.awt.Graphics;


public abstract class GridMarker extends Marker {
	

	public GridMarker(Colour colour, String shape, GridPoint point, MarkerOrientation orientation)
	{
		super(point);
		this.shape = shape;
		this.colour = colour;
		this.point = point;
		this.orientation = orientation;
	}
	
	public GridMarker(Colour colour, String shape, GridPoint point)
	{
		this(colour, shape, point, null);
	}
	
	public GridMarker(Colour colour, String shape)
	{
		this(colour, shape, null, null);
	}
	
	public GridMarker()
	{
		this(null, null, null, null);
	}
	
	public abstract void drawMarker(Graphics g, double x, double y);
	
	protected String shape;
	
	public String getShape()
	{
		return shape;
	}
	
	public void setShape(String shape)
	{
		this.shape = shape;
	}
	
	protected Colour colour;
	
	public Colour getColour()
	{
		return colour;
	}
	
	public void setColour(Colour colour)
	{
		this.colour = colour;
	}
	
	private GridPoint point;
	
	public GridPoint getPoint()
	{
		return point;
	}

	public void setPoint(GridPoint point)
	{
		this.point = point;
	}
	
	public GridPoint findMidpointBetween(GridMarker other)
	{
		return this.point.findMidpointBetween(other.getPoint());
	}
	
	protected MarkerOrientation orientation;
	
	public MarkerOrientation getOrientation()
	{
		return this.orientation;
	}
	
	public void setOrientation(MarkerOrientation orientation)
	{
		this.orientation = orientation;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((colour == null) ? 0 : colour.hashCode());
		result = prime * result + ((shape == null) ? 0 : shape.hashCode());
		return result;
	}

	/**
	 * Vergelijkt twee GridMarkers, gebaseerd op hun shape en hun colour
	 */
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
	
	public boolean equalsWithOrientation(Object obj)
	{
		return this.equals(obj) && this.getOrientation() == ((GridMarker) obj).getOrientation();
	}
	
	@Override
	public String toString()
	{
		return colour + " " + shape + " " + getPoint().toString();
	}

	public boolean matchColor(GridMarker other) {
		if (colour == null) {
			if (other.colour != null)
				return false;
		} else if (!colour.equals(other.colour))
			return false;
		return true;
	}
}
