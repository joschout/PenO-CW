package coordinate;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.Ellipse2D;

public class Heart extends GridMarker {

	
	public Heart(Colour colour, String shape, GridPoint point){
		super( colour, shape, point);
	}
	
	public Heart(Colour colour, String shape){
		super( colour, shape, null);
	}
	
	
	public void drawMarker(Graphics g){
		drawMarker(g, this.getPoint().x, this.getPoint().y);
	}
	
	/**
	 * double startX=10;
	 * double startY=60;
	 */
	@Override
	public void drawMarker(Graphics g, double X, double Y) {
		
		Graphics2D g2 = (Graphics2D)g;
		
		double a = 2.5;
		
		double startX = X - 2.5*a;
		double startY = Y - 2.25*a;
		
		// First circle
		Ellipse2D circle1 = new Ellipse2D.Double(startX, startY, 3*a, 3*a);
		g2.setColor(this.getColour().AWTColor());
		g2.draw(circle1);
		g2.fill(circle1);

		// Second circle
		Ellipse2D circle2 = new Ellipse2D.Double(startX+2*a, startY, 3*a, 3*a);
		g2.setColor(this.getColour().AWTColor());
		g2.draw(circle2);
		g2.fill(circle2);
		
		// Draw a diamond
		Polygon polygon = new Polygon();
		polygon.addPoint((int)(startX+0.4*a), (int)(startY+2.5*a));		
		polygon.addPoint((int)(startX+(5*a)/2), (int)(startY+4.5*a));
		polygon.addPoint((int) (startX+4.6*a),(int)(startY+2.5*a));
		polygon.addPoint((int)(startX+(5.0*a)/2), (int)(startY+1.5*a));		
		g2.setColor(this.getColour().AWTColor());
		g2.draw(polygon);
		g2.fill(polygon);
		
	}

}
