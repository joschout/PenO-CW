package zeppelin;

import coordinate.GridPoint;

public interface IZeppelin {

	
	public GridPoint getPosition();
	public void setPosition(GridPoint point);
	
	public double getHeight();
	public void setHeight(double height);
}
