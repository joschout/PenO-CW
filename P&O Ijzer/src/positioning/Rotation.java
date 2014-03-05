package positioning;

import java.awt.Image;

import coordinate.GridMarker;

public class Rotation {
	
	public Image image;
	public GridMarker leftMarker;
	public GridMarker rightMarker;

	public void readImage() {
		
	}
	
	public double getRealRotation(GridMarker leftMarker, GridMarker rightMarker) {
		double imageHeight = image.getHeight();
		double newLeftMarkerY = -(leftMarker.getY() - imageHeight); //veranderen van assenstelsel
		double newRightMarkerY = -(rightMarker.getY() - imageHeight);
		
		//Vectoren projecteren naar oorsprong van het assenstelsel
		GridMarker leftMarkerProjection.setCoordinates(0, 0);
		GridMarker rightMarkerProjection.setCoordinates(rightMarker.getX() - leftMarker.getX(), newRightMarkerY - newLeftMarkerY);
		
		double sProduct = rightMarker.getX();
		double angle = Math.acos(sProduct/(Math.sqrt(Math.pow(rightMarkerProjection.getX(), 2) + Math.pow(rightMarkerProjection.getY(), 2))));
		
		if(angle > 180) {
			angle = 360 - angle;
			angle = -angle;
		}
				
	}

}
