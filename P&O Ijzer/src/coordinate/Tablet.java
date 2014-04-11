package coordinate;

public class Tablet {
	
	String name;
	double x;
	double y;

	public Tablet(String name, String x, String y) {
		this.name = name;
		this.x = Integer.parseInt(x);
		this.y = Integer.parseInt(y);
	}

}
