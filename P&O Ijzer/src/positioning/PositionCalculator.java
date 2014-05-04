
package positioning;


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
		
		GridPoint newPicturePoint = this.picturePoint.subtract(imageCenter);
//		GridPoint pictureRotated = newPicturePoint.rotate(360 - angle);
		GridPoint pictureRotated = newPicturePoint.rotate(angle - 90);
//		GridPoint pictureRotated = newPicturePoint.rotate(0);
		GridPoint pictureScaled = pictureRotated.multiply(1 / image.getPixelLength());
		
		return new GridPoint(trianglePoint.x - pictureScaled.x, trianglePoint.y - pictureScaled.y);
//		return trianglePoint;
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
