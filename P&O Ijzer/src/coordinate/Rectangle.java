package coordinate;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public class Rectangle extends GridMarker {

	public Rectangle(Colour colour, String shape, GridPoint point){
		super( colour, shape, point);
	}
	
	public Rectangle(Colour colour, String shape){
		super( colour, shape, null);
	}
	
	public void drawMarker(Graphics g){
		drawMarker(g, this.getPoint().x, this.getPoint().y);
	}
	
	@Override
	public void drawMarker(Graphics g, double x, double y) {
		

		Graphics2D g2 = (Graphics2D)g;
		int w=10;
		int h=10;
		
		
		Rectangle2D rectangle1 = new Rectangle2D.Double(x-w/2, y-h/2, w, h);
		g2.setColor(this.getColour().AWTColor());
		g2.draw(rectangle1);
		g2.fill(rectangle1);

	}

}
