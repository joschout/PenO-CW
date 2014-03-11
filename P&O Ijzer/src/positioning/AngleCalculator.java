package positioning;

import java.util.ArrayList;
import java.util.List;

import zeppelin.MainProgramImpl;
import coordinate.Grid;
import coordinate.GridMarker;
import coordinate.GridPoint;
import coordinate.GridTriangle;
import coordinate.MarkerOrientation;

public class AngleCalculator {
	
	//TODO Triangle klasse integreren!
	
	private Image image;
	
	private List<GridMarker> pictureMarkers;
	private List<GridMarker> triangleMarkers;
	
	// Lijst van offsets
	private static double LEFT_RIGHT_OFFSET = 0;
	private static double UP_LEFT_OFFSET = 120;
	private static double UP_RIGHT_OFFSET = 60;
	private static double DOWN_LEFT_OFFSET = 240;
	private static double DOWN_RIGHT_OFFSET = 300;
	
	public AngleCalculator(Image image, Couple pictureCouple, Couple triangleCouple)
	{
		this.image = image;
		matchMarkers(pictureCouple, triangleCouple);
	}

	/**
	 * Vraag aan de PositionCalculator om de hoek en de positie van de zeppelin te updaten.
	 * @param fileName
	 * 		Naam van de foto op basis van dewelke de nieuwe positie van de zeppelin
	 * 		wordt berekend.
	 */
	public double calculateAngle()
	{
		return calculateAngleCouple();
	}
	
	/**
	 * Bereken de hoek in het cartesisch assenstelsel op basis van de gegeven coördinaten.
	 * Hierbij wordt de top van de eenheidscirkel beschouwd als 0 graden en komt draaien in wijzerzin
	 * overeen met een optelling.
	 * @param y
	 * 		Verticale coördinaat.
	 * @param x
	 * 		Horizontale coördinaat.
	 * @param offset
	 * 		Wordt bij de hoek geteld vóór conversie naar een positieve hoek.
	 * @return	De hoek van het punt gespecifieerd door y en x tegenover de oorsprong in graden.
	 */
	public static double angleWithOffset(double y, double x, double offset)
	{
		double angle = Math.toDegrees(Math.atan2(y, x));
		angle += offset;
		angle = angle % 360;
		angle = angle * (-1);
		if (angle < 0)
		{
			angle += 360;
		}
		return (angle % 360);
	}
	
//	/**
//	 * Berekent de hoek op basis van het linkerpunt en het rechterpunt van de driehoek.
//	 * @param foundMarkers
//	 * 		De GridMarkers gevonden in de foto.
//	 * @param equivalentMarkers
//	 * 		De GridMarkers in het rooster die overeenkomen met foundMarkers
//	 * @return
//	 * 		De hoek in graden van de zeppelin.
//	 */
//	private double calculateAngleThreeMarkers(List<GridMarker> foundMarkers, List<GridMarker> equivalentMarkers)
//	{
//		GridMarker pictureLeftMarker = findLeftMarker(foundMarkers, equivalentMarkers);
//		GridMarker pictureRightMarker = findRightMarker(foundMarkers, equivalentMarkers);
//		GridPoint pictureLeftPoint = pictureLeftMarker.getPoint();
//		GridPoint pictureRightPoint = pictureRightMarker.getPoint();
//		
//		double newLeftY = - (pictureLeftPoint.y - image.getHeight());
//		double newRightY = - (pictureRightPoint.y - image.getHeight());
//	
//		GridPoint rightProjection = new GridPoint(pictureRightPoint.x - pictureLeftPoint.x, newRightY - newLeftY);
//		
//		return angleWithOffset(rightProjection.y, rightProjection.x, 0);
//	}
	
	/**
	 * Berekent de hoek op basis van de twee punten in de driehoek die zichtbaar zijn (this is where the magic begins).
	 * @param foundMarkers
	 * 		Zie calculateAngleThreeMarkers
	 * @param equivalentMarkers
	 * 		Zie calculateAngleThreeMarkers
	 * @return
	 * 		De hoek in graden van de zeppelin.
	 */
	private double calculateAngleCouple()
	{
		GridMarker oneTriangleMarker = triangleMarkers.get(0);
		GridMarker otherTriangleMarker = triangleMarkers.get(1);
		GridMarker onePictureMarker = pictureMarkers.get(0);
		GridMarker otherPictureMarker = pictureMarkers.get(1);
		MarkerOrientation orientationOfOne = oneTriangleMarker.getOrientation();
		MarkerOrientation orientationOfOther = otherTriangleMarker.getOrientation();
		// Controleer welke van de zes gevallen zich voordoet (zie driehoekjes.png en driehoekjesdown.png
		// in AangepastTussentijdsVerslag-sem2 in de Dropbox)
		
		// geval 1a: de lijn van links naar rechts
		if (orientationOfOne == MarkerOrientation.LEFT && orientationOfOther == MarkerOrientation.RIGHT)
		{
			return performFinalCalculation(onePictureMarker, otherPictureMarker, LEFT_RIGHT_OFFSET);
		}
		// geval 1a omgekeerd
		if (orientationOfOne == MarkerOrientation.RIGHT && orientationOfOther == MarkerOrientation.LEFT)
		{
			return performFinalCalculation(otherPictureMarker, onePictureMarker, LEFT_RIGHT_OFFSET);
		}
		// geval 1b: de lijn van bovenpunt naar linkerpunt
		if (orientationOfOne == MarkerOrientation.UP && orientationOfOther == MarkerOrientation.LEFT)
		{
			return performFinalCalculation(onePictureMarker, otherPictureMarker, UP_LEFT_OFFSET);
		}
		// geval 1b omgekeerd
		if (orientationOfOne == MarkerOrientation.LEFT && orientationOfOther == MarkerOrientation.UP)
		{
			return performFinalCalculation(otherPictureMarker, onePictureMarker, UP_LEFT_OFFSET);
		}
		// geval 1c: de lijn van bovenpunt naar rechterpunt
		if (orientationOfOne == MarkerOrientation.UP && orientationOfOther == MarkerOrientation.RIGHT)
		{
			return performFinalCalculation(onePictureMarker, otherPictureMarker, UP_RIGHT_OFFSET);
		}
		// geval 1c omgekeerd
		if (orientationOfOne == MarkerOrientation.RIGHT && orientationOfOther == MarkerOrientation.UP)
		{
			return performFinalCalculation(otherPictureMarker, onePictureMarker, UP_RIGHT_OFFSET);
		}
		// geval 2a: de lijn van onderpunt naar linkerpunt
		if (orientationOfOne == MarkerOrientation.DOWN && orientationOfOther == MarkerOrientation.LEFT)
		{
			return performFinalCalculation(onePictureMarker, otherPictureMarker, DOWN_LEFT_OFFSET);
		}
		// geval 2a omgekeerd
		if (orientationOfOne == MarkerOrientation.LEFT && orientationOfOther == MarkerOrientation.DOWN)
		{
			return performFinalCalculation(otherPictureMarker, onePictureMarker, DOWN_LEFT_OFFSET);
		}
		// geval 2b: de lijn van onderpunt naar rechterpunt
		if (orientationOfOne == MarkerOrientation.DOWN && orientationOfOther == MarkerOrientation.RIGHT)
		{
			return performFinalCalculation(onePictureMarker, otherPictureMarker, DOWN_RIGHT_OFFSET);
		}
		// geval 2b omgekeerd
		else // (oneOr == MarkerOrientation.RIGHT && otherOr == MarkerOrientation.DOWN)
		{
			return performFinalCalculation(otherPictureMarker, onePictureMarker, DOWN_RIGHT_OFFSET);
		}
		// geval 2c moet niet gecontroleerd worden omdat die overeenkomt met 1a
	}
	
	private void matchMarkers(Couple pictureCouple, Couple triangleCouple)
	{
		this.pictureMarkers = new ArrayList<GridMarker>();
		this.triangleMarkers = new ArrayList<GridMarker>();
		GridMarker pictureMarker1 = pictureCouple.getMarker1();
		GridMarker triangleMarker1 = triangleCouple.getMarker1();
		if (pictureMarker1.equals(triangleMarker1))
		{
			pictureMarkers.add(pictureMarker1);
			pictureMarkers.add(pictureCouple.getMarker2());
			triangleMarkers.add(triangleCouple.getMarker1());
			triangleMarkers.add(triangleCouple.getMarker2());
		}
		else
		{
			pictureMarkers.add(pictureCouple.getMarker2());
			pictureMarkers.add(pictureMarker1);
			triangleMarkers.add(triangleCouple.getMarker2());
			triangleMarkers.add(triangleCouple.getMarker1());
		}
	}
	
//	private GridMarker findLeftMarker(List<GridMarker> foundMarkers, List<GridMarker> equivalentMarkers)
//	{
//		for (GridMarker pictureMarker : foundMarkers)
//		{
//			GridMarker gridMarker = equivalentMarkers.get(equivalentMarkers.indexOf(pictureMarker));
//			if (gridMarker.getOrientation() == MarkerOrientation.LEFT)
//			{
//				return pictureMarker;
//			}
//		}
//		return null;
//	}
	
//	private GridMarker findRightMarker(List<GridMarker> foundMarkers, List<GridMarker> equivalentMarkers)
//	{
//		for (GridMarker pictureMarker : foundMarkers)
//		{
//			GridMarker gridMarker = equivalentMarkers.get(equivalentMarkers.indexOf(pictureMarker));
//			if (gridMarker.getOrientation() == MarkerOrientation.RIGHT)
//			{
//				return pictureMarker;
//			}
//		}
//		return null;
//	}
	
	private double performFinalCalculation(GridMarker originMarker, GridMarker otherMarker, double angleOffset)
	{
		GridPoint originPoint = originMarker.getPoint();
		GridPoint otherPoint = otherMarker.getPoint();
		
		double newOriginY =  - (originPoint.y - image.getHeight());
		double newOtherY = - (otherPoint.y - image.getHeight());
		
		GridPoint otherProjection = new GridPoint(otherPoint.x - originPoint.x, newOtherY - newOriginY);
		return angleWithOffset(otherProjection.y, otherProjection.x, angleOffset);
	}
}
