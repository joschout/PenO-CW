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
//		GridPoint imageCenter = image.getCenterCoordinatesOfImage();
//		double newPictureY = - (this.picturePoint.y - image.getHeight());
//		
//		GridPoint newPicturePoint = new GridPoint(this.picturePoint.x, this.picturePoint.y);
//		
//		GridPoint imageRotated = imageCenter.rotate(360 - (angle + 90)); // 360 - (angle + 90))
//		GridPoint pictureRotated = newPicturePoint.rotate(360 - (angle + 90));
//		
//		double diffX = (imageRotated.x - pictureRotated.x) / image.getPixelLength();
//		double baseDiffY = - ((imageRotated.y - pictureRotated.y) - image.getHeight());
//		double diffY = baseDiffY / image.getPixelLength();
//		
//		return new GridPoint(trianglePoint.x + diffX, trianglePoint.y + diffY);
		return trianglePoint;
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
