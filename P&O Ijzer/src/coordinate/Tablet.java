package coordinate;

public class Tablet {
	
	int id;
//	double x;
//	double y;
 
	private GridPoint position;
	
	public GridPoint getPosition(){
		return position;				
	}
	public void setPosition(GridPoint point){
		this.position=point;
	}
	
	
	
	public Tablet(int id, double x, double y) {
		this.id = id;
		setPosition(new GridPoint(x, y));
	}
	
	public Tablet(int id, GridPoint position) {
		this.id = id;
		setPosition(position);
	}
	
	
	
//	public Tablet(int name, String x, String y) {
//		this.name = name;
//		this.x = Integer.parseInt(x);
//		this.y = Integer.parseInt(y);
//	}
	
	
	public String toString()
	{
		String toReturn = ("Tablet: " + this.id + "      x: " + this.getPosition().x + "       y: " + this.getPosition().y);
		return toReturn;
	}
public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}

public String getName() {
	return "tablet" + id;
}
	

}
