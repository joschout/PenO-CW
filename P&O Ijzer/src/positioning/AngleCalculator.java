package positioning;

import java.util.List;

import zeppelin.MainProgramImpl;
import coordinate.Grid;
import coordinate.GridMarker;
import coordinate.GridPoint;
import coordinate.GridTriangle;
import coordinate.MarkerOrientation;

public class AngleCalculator {
	
	//TODO Triangle klasse integreren!
	
	
	private MainProgramImpl zeppelin;
	private Grid grid;
	private Image image;
	private ImageAnalyser analyser;
	
	// Lijst van offsets
	private static double LEFT_RIGHT_OFFSET = 0;
	private static double UP_LEFT_OFFSET = 120;
	private static double UP_RIGHT_OFFSET = 60;
	private static double DOWN_LEFT_OFFSET = 240;
	private static double DOWN_RIGHT_OFFSET = 300;
	
	private double triangleSideLength = 40;
	
	public AngleCalculator(Grid grid, Image image)
	{
		this.grid = grid;
		this.analyser = new ImageAnalyser(image);
		this.image = image;
	}

	/**
	 * Vraag aan de PositionCalculator om de hoek en de positie van de zeppelin te updaten.
	 * @param fileName
	 * 		Naam van de foto op basis van dewelke de nieuwe positie van de zeppelin
	 * 		wordt berekend.
	 */
	public void updatePositionAndAngle()
	{
		List<GridMarker> foundMarkers = analyser.analysePicture();
		GridTriangle triangle = grid.getBestMatch(foundMarkers, zeppelin.getPosition());
		List<GridMarker> equivalentMarkers = triangle.getEquivalentMarkers(foundMarkers);
		foundMarkers.retainAll(equivalentMarkers);
		double angle;
		if (equivalentMarkers.size() == 3)
		{
			angle = calculateAngleThreeMarkers(foundMarkers, equivalentMarkers);
			zeppelin.setAngle(angle);
		}
		else if (equivalentMarkers.size() == 2)
		{
			angle = calculateAngleTwoMarkers(foundMarkers, equivalentMarkers);
			zeppelin.setAngle(angle);
		}
		else
		{
			// geen zinnige informatie te halen uit enkel één marker
			return;
		}
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
	
	/**
	 * Berekent de hoek op basis van het linkerpunt en het rechterpunt van de driehoek.
	 * @param foundMarkers
	 * 		De GridMarkers gevonden in de foto.
	 * @param equivalentMarkers
	 * 		De GridMarkers in het rooster die overeenkomen met foundMarkers
	 * @return
	 * 		De hoek in graden van de zeppelin.
	 */
	private double calculateAngleThreeMarkers(List<GridMarker> foundMarkers, List<GridMarker> equivalentMarkers)
	{
		GridMarker pictureLeftMarker = findLeftMarker(foundMarkers, equivalentMarkers);
		GridMarker pictureRightMarker = findRightMarker(foundMarkers, equivalentMarkers);
		GridPoint pictureLeftPoint = pictureLeftMarker.getPoint();
		GridPoint pictureRightPoint = pictureRightMarker.getPoint();
		
		double newLeftY = - (pictureLeftPoint.y - image.getHeight());
		double newRightY = - (pictureRightPoint.y - image.getHeight());
	
		GridPoint rightProjection = new GridPoint(pictureRightPoint.x - pictureLeftPoint.x, newRightY - newLeftY);
		
		return angleWithOffset(rightProjection.y, rightProjection.x, 0);
	}
	
	/**
	 * Berekent de hoek op basis van de twee punten in de driehoek die zichtbaar zijn (this is where the magic begins).
	 * @param foundMarkers
	 * 		Zie calculateAngleThreeMarkers
	 * @param equivalentMarkers
	 * 		Zie calculateAngleThreeMarkers
	 * @return
	 * 		De hoek in graden van de zeppelin.
	 */
	private double calculateAngleTwoMarkers(List<GridMarker> foundMarkers, List<GridMarker> equivalentMarkers)
	{
		
		GridMarker oneMarker = equivalentMarkers.get(0);
		GridMarker otherMarker = equivalentMarkers.get(1);
		MarkerOrientation orientationOfOne = oneMarker.getOrientation();
		MarkerOrientation orientationOfOther = otherMarker.getOrientation();
		// Controleer welke van de zes gevallen zich voordoet (zie driehoekjes.png en driehoekjesdown.png
		// in AangepastTussentijdsVerslag-sem2 in de Dropbox)
		
		// geval 1a: de lijn van links naar rechts
		if (orientationOfOne == MarkerOrientation.LEFT && orientationOfOther == MarkerOrientation.RIGHT)
		{
			return performFinalCalculation(oneMarker, otherMarker, LEFT_RIGHT_OFFSET);
		}
		// geval 1a omgekeerd
		if (orientationOfOne == MarkerOrientation.RIGHT && orientationOfOther == MarkerOrientation.LEFT)
		{
			return performFinalCalculation(otherMarker, oneMarker, LEFT_RIGHT_OFFSET);
		}
		// geval 1b: de lijn van bovenpunt naar linkerpunt
		if (orientationOfOne == MarkerOrientation.UP && orientationOfOther == MarkerOrientation.LEFT)
		{
			return performFinalCalculation(oneMarker, otherMarker, UP_LEFT_OFFSET);
		}
		// geval 1b omgekeerd
		if (orientationOfOne == MarkerOrientation.LEFT && orientationOfOther == MarkerOrientation.UP)
		{
			return performFinalCalculation(otherMarker, oneMarker, UP_LEFT_OFFSET);
		}
		// geval 1c: de lijn van bovenpunt naar rechterpunt
		if (orientationOfOne == MarkerOrientation.UP && orientationOfOther == MarkerOrientation.RIGHT)
		{
			return performFinalCalculation(oneMarker, otherMarker, UP_RIGHT_OFFSET);
		}
		// geval 1c omgekeerd
		if (orientationOfOne == MarkerOrientation.RIGHT && orientationOfOther == MarkerOrientation.UP)
		{
			return performFinalCalculation(otherMarker, oneMarker, UP_RIGHT_OFFSET);
		}
		// geval 2a: de lijn van onderpunt naar linkerpunt
		if (orientationOfOne == MarkerOrientation.DOWN && orientationOfOther == MarkerOrientation.LEFT)
		{
			return performFinalCalculation(oneMarker, otherMarker, DOWN_LEFT_OFFSET);
		}
		// geval 2a omgekeerd
		if (orientationOfOne == MarkerOrientation.LEFT && orientationOfOther == MarkerOrientation.DOWN)
		{
			return performFinalCalculation(otherMarker, oneMarker, DOWN_LEFT_OFFSET);
		}
		// geval 2b: de lijn van onderpunt naar rechterpunt
		if (orientationOfOne == MarkerOrientation.DOWN && orientationOfOther == MarkerOrientation.RIGHT)
		{
			return performFinalCalculation(oneMarker, otherMarker, DOWN_RIGHT_OFFSET);
		}
		// geval 2b omgekeerd
		else // (oneOr == MarkerOrientation.RIGHT && otherOr == MarkerOrientation.DOWN)
		{
			return performFinalCalculation(otherMarker, oneMarker, DOWN_RIGHT_OFFSET);
		}
		// geval 2c moet niet gecontroleerd worden omdat die overeenkomt met 1a
	}
	
	private GridMarker findLeftMarker(List<GridMarker> foundMarkers, List<GridMarker> equivalentMarkers)
	{
		for (GridMarker pictureMarker : foundMarkers)
		{
			GridMarker gridMarker = equivalentMarkers.get(equivalentMarkers.indexOf(pictureMarker));
			if (gridMarker.getOrientation() == MarkerOrientation.LEFT)
			{
				return pictureMarker;
			}
		}
		return null;
	}
	
	private GridMarker findRightMarker(List<GridMarker> foundMarkers, List<GridMarker> equivalentMarkers)
	{
		for (GridMarker pictureMarker : foundMarkers)
		{
			GridMarker gridMarker = equivalentMarkers.get(equivalentMarkers.indexOf(pictureMarker));
			if (gridMarker.getOrientation() == MarkerOrientation.RIGHT)
			{
				return pictureMarker;
			}
		}
		return null;
	}
	
	private double performFinalCalculation(GridMarker originMarker, GridMarker otherMarker, double angleOffset)
	{
		GridPoint originPoint = originMarker.getPoint();
		GridPoint otherPoint = otherMarker.getPoint();
		
		double newOriginY =  - (originPoint.y - image.getHeight());
		double newOtherY = - (otherPoint.y - image.getHeight());
		
		GridPoint otherProjection = new GridPoint(otherPoint.x - originPoint.x, newOtherY - newOriginY);
		return angleWithOffset(otherProjection.y, otherProjection.x, 0);
	}
	
	// TODO: refactor naar een position calculator klasse
	private double[][] makeRotationMatrix(double angle)
	{
		// 360 - angle, want er is een afwijking van "angle graden" naar rechts, dus ga naar links
		double[][] toReturn = new double[2][2];
		toReturn[0][0] = Math.cos(Math.toRadians(360 - angle));
		toReturn[0][1] = Math.sin(Math.toRadians(360 - angle));
		toReturn[1][0] = - Math.sin(Math.toRadians(360 - angle));
		toReturn[0][1] = Math.cos(Math.toRadians(360 - angle));
		return toReturn;
	}
}
