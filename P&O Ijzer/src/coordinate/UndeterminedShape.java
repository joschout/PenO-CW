package coordinate;

import java.awt.Graphics;

public class UndeterminedShape extends GridMarker {
	
	public UndeterminedShape(Colour colour, String shape, GridPoint point){
		super( colour, shape, point);
	}
	
	public UndeterminedShape(Colour colour, String shape){
		super( colour, shape, null);
	}

	@Override
	public void drawMarker(Graphics g, double x, double y) {
		
	}

	@Override
	public void drawMarker(Graphics g) {
		
		
	}

}
