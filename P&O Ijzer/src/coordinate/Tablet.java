package coordinate;

public class Tablet {
	
	int name;
	double x;
	double y;

	public Tablet(int name, String x, String y) {
		this.name = name;
		this.x = Integer.parseInt(x);
		this.y = Integer.parseInt(y);
	}
	
	
	public String toString()
	{
		String toReturn = ("Tablet: " + this.name + "      x: " + this.x + "       y: " + this.y);
		return toReturn;
	}
	

}
