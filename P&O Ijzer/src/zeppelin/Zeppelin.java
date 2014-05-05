package zeppelin;

import coordinate.GridPoint;

public class Zeppelin implements IZeppelin {
	public Zeppelin(){
		this(new GridPoint(0, 0), (double)0);
	}
	
	public Zeppelin(GridPoint position, double height){
		this.position=position;
		this.height=height;	
	}

	private GridPoint position;
	private double height;
	
	public GridPoint getPosition(){
		return position;				
	}
	public void setPosition(GridPoint point){
		this.position=point;
	}
	
	public double getHeight(){
		return height;
	}
	public void setHeight(double height){
		this.height=height;
	}
}
