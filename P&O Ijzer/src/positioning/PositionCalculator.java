package positioning;

import java.util.ArrayList;

import coordinate.GridMarker;
import coordinate.GridPoint;

public class PositionCalculator {
	
	private Image image;
	private GridPoint trianglePoint;
	private GridPoint picturePoint;
	
	public PositionCalculator(Image image, Couple pictureCouple, Couple triangleCouple)
	{
		this.image = image;
		this.matchMarkers(pictureCouple, triangleCouple);
	}
	
	public GridPoint calculatePosition(double angle)
	{
		GridPoint imageCenter = image.getCenterCoordinatesOfImage();
		double newPictureY = - (this.picturePoint.y - image.getHeight());
		
		GridPoint newPicturePoint = new GridPoint(this.picturePoint.x, newPictureY);
		
		GridPoint imageRotated = imageCenter.rotate(angle);
		GridPoint pictureRotated = newPicturePoint.rotate(angle);
		
		double diffX = (imageRotated.x - pictureRotated.x) * image.getPixelLength();
		double diffY = (imageRotated.y - pictureRotated.y) * image.getPixelLength();
		
		return new GridPoint(trianglePoint.x + diffX, trianglePoint.y + diffY);
	}
	
	private void matchMarkers(Couple pictureCouple, Couple triangleCouple)
	{
		GridMarker pictureMarker1 = pictureCouple.getMarker1();
		GridMarker triangleMarker1 = triangleCouple.getMarker1();
		if (pictureMarker1.equals(triangleMarker1))
		{
			this.picturePoint = pictureMarker1.getPoint();
			this.trianglePoint = triangleMarker1.getPoint();;
		}
		else
		{
			this.picturePoint = pictureMarker1.getPoint();
			this.trianglePoint = triangleCouple.getMarker2().getPoint();
		}
	}
}
