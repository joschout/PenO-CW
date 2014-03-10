package coordinate;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;

public class Star extends GridMarker {

	public Star(Colour colour, String shape, GridPoint point){
		super( colour, shape, point);
	}
	
	public Star(Colour colour, String shape){
		super( colour, shape, null);
	}
	
	
	
	
	public void drawMarker(Graphics g){
		drawMarker(g, this.getPoint().x, this.getPoint().y);
	}
	
	@Override
	public void drawMarker(Graphics g, double x, double y) {
		Graphics2D g2 = (Graphics2D)g;
		double outerRadius = 10;
		double innerRadius =5;
		
		Shape starShape = createStar(5, new GridPoint(x, y), outerRadius, innerRadius);
		g2.setColor(this.getColour().AWTColor());
		g2.draw(starShape);	
		g2.fill(starShape);
	}

	
	public static Shape createStar(int arms, GridPoint center, double rOuter, double rInner)
	{
	    double angle = Math.PI / arms;

	    GeneralPath path = new GeneralPath();

	    for (int i = 0; i < 2 * arms; i++)
	    {
	        double r = (i & 1) == 0 ? rOuter : rInner;
	        Point2D.Double p = new Point2D.Double(center.x + Math.cos(i * angle) * r, center.y + Math.sin(i * angle) * r);
	        if (i == 0) path.moveTo(p.getX(), p.getY());
	        else path.lineTo(p.getX(), p.getY());
	    }
	    path.closePath();
	    return path;
	}
}
