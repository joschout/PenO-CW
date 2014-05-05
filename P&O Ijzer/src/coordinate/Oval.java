package coordinate;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

public class Oval extends GridMarker {

	public Oval(Colour colour, String shape, GridPoint point){
		super( colour, shape, point);
	}
	
	public Oval(Colour colour, String shape){
		super( colour, shape, null);
	}
	
	
	public void drawMarker(Graphics g){
		drawMarker(g, this.getPoint().x, this.getPoint().y);
		
	}
	
	
	@Override
	public void drawMarker(Graphics g, double x, double y) {

		
		Graphics2D g2 = (Graphics2D)g;
		
		double a = 5;
		double w = 2*a;
		double h = a;

		
		double startX = x - w/2;
		double startY = y - h/2;
		
		// First circle
		Ellipse2D circle1 = new Ellipse2D.Double(startX, startY, w, h);
		g2.setColor(this.getColour().AWTColor());
		g2.draw(circle1);
		g2.fill(circle1);
		
	}

}
