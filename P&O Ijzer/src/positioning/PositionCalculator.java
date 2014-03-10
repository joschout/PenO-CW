package positioning;

import java.util.ArrayList;

import coordinate.GridMarker;
import coordinate.GridPoint;

public class PositionCalculator {
	
	private Image image;
	private GridMarker pictureMarker;
	private GridMarker triangleMarker;
	
	public PositionCalculator(Image image, Couple pictureCouple, Couple triangleCouple)
	{
		this.image = image;
		this.matchMarkers(pictureCouple, triangleCouple);
	}
	
	public GridPoint calculatePosition(double angle)
	{
		
	}
	
	private void matchMarkers(Couple pictureCouple, Couple triangleCouple)
	{
		GridMarker pictureMarker1 = pictureCouple.getMarker1();
		GridMarker triangleMarker1 = triangleCouple.getMarker1();
		if (pictureMarker1.equals(triangleMarker1))
		{
			this.pictureMarker = pictureMarker1;
			this.triangleMarker = triangleMarker1;
		}
		else
		{
			this.pictureMarker = pictureCouple.getMarker1();
			this.triangleMarker = triangleCouple.getMarker2();
		}
	}

}
