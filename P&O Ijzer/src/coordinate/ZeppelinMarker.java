package coordinate;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.color.*;
import java.awt.geom.Ellipse2D;

public class ZeppelinMarker extends Marker {
	
	public ZeppelinMarker(GridPoint point) {
		super(point);	
		setMarkerColor(Color.darkGray);
	}
	
	public ZeppelinMarker(GridPoint point, Color color) {
		super(point);	
		setMarkerColor(color);
	}
	
	public void drawMarker(Graphics g){
		drawMarker(g, this.getPoint().x, this.getPoint().y);
	}

	public  void drawMarker(Graphics g, double x, double y){
		Graphics2D g2 = (Graphics2D)g;
		
		double a = 10;
		double w = 2*a;
		
	
		
		double startX = x - w/2;
		double startY = y - w/2;
		
		// First circle
		Ellipse2D circle1 = new Ellipse2D.Double(startX, startY, w, w);
		g2.setColor(getMarkerColor());
		g2.draw(circle1);
		g2.drawLine((int)x, (int)(y-w/2), (int)x,(int)(y+w/2));
		g2.drawLine((int)(x-w/2), (int)y, (int)(x+w/2), (int)y);
		
	}



	public Color getMarkerColor() {
		return markerColor;
	}



	private Color markerColor;



	public void setMarkerColor(Color markerColor) {
		this.markerColor = markerColor;
	}
	
	
	

	

}
