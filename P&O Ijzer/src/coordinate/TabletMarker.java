package coordinate;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public class TabletMarker extends Marker {

	
	public TabletMarker(GridPoint point) {
		super(point);	
		setMarkerColor(Color.BLACK);
	}
	
	public TabletMarker(GridPoint point, Color color) {
		super(point);	
		setMarkerColor(color);
	}


	public void drawMarker(Graphics g){
		drawMarker(g, this.getPoint().x, this.getPoint().y);
	}
	
	@Override
	public void drawMarker(Graphics g, double x, double y) {
		Graphics2D g2 = (Graphics2D)g;
		int w=20;
		int h=10;
		
		
		Rectangle2D rectangle1 = new Rectangle2D.Double(x-w/2, y-h/2, w, h);
		g2.setColor(getMarkerColor());
		g2.draw(rectangle1);
		g2.fill(rectangle1);
	}
	
	public Color getMarkerColor() {
		return markerColor;
	}

	private Color markerColor;

	public void setMarkerColor(Color markerColor) {
		this.markerColor = markerColor;
	}
	
	
}
