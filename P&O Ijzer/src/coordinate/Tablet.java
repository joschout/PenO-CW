package coordinate;

public class Tablet {
	
	int name;
//	double x;
//	double y;
 
	private GridPoint position;
	
	public GridPoint getPosition(){
		return position;				
	}
	public void setPosition(GridPoint point){
		this.position=point;
	}
	
	
	
	public Tablet(int name, double x, double y) {
		this.name = name;
		setPosition(new GridPoint(x, y));
	}
	
	public Tablet(int name, GridPoint position) {
		this.name = name;
		setPosition(position);
	}
	
	
	
//	public Tablet(int name, String x, String y) {
//		this.name = name;
//		this.x = Integer.parseInt(x);
//		this.y = Integer.parseInt(y);
//	}
	
	
	public String toString()
	{
		String toReturn = ("Tablet: " + this.name + "      x: " + this.getPosition().x + "       y: " + this.getPosition().y);
		return toReturn;
	}
	

}
