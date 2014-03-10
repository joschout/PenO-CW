package coordinate;

import java.awt.Graphics;

public abstract class Marker {

	public Marker(GridPoint point){
		this.point= point;
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
	
	public abstract void drawMarker(Graphics g);
	
	public abstract void drawMarker(Graphics g, double x, double y);
	
}
