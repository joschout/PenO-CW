package positioning;

import java.util.List;

import zeppelin.MainProgramImpl;
import coordinate.Grid;
import coordinate.GridMarker;
import coordinate.GridPoint;
import coordinate.GridTriangle;
import coordinate.MarkerOrientation;

public class PositionCalculator {
	
	private MainProgramImpl zeppelin;
	private Grid grid;
	private Image image;
	private ImageAnalyser analyser;
	
	private double triangleSideLength = 40;
	
	public PositionCalculator(Grid grid)
	{
		this.grid = grid;
		this.analyser = new ImageAnalyser();
	}

	/**
	 * Vraag aan de PositionCalculator om de hoek en de positie van de zeppelin te updaten.
	 * @param fileName
	 * 		Naam van de foto op basis van dewelke de nieuwe positie van de zeppelin
	 * 		wordt berekend.
	 */
	public void updatePositionAndAngle(String fileName)
	{
		image = new Image(fileName + ".jpg");
		List<GridMarker> foundMarkers = analyser.analysePicture(fileName);
		GridTriangle triangle = grid.getBestMatch(foundMarkers, zeppelin.getPosition());
		List<GridMarker> equivalentMarkers = triangle.getEquivalentMarkers(foundMarkers);
		foundMarkers.retainAll(equivalentMarkers);
		double angle;
		if (equivalentMarkers.size() == 3)
		{
			angle = calculateAngleThreeMarkers(foundMarkers, equivalentMarkers);
			zeppelin.updateMostRecentAngle(angle);
		}
		else if (equivalentMarkers.size() == 2)
		{
			angle = calculateAngleTwoMarkers(foundMarkers, equivalentMarkers);
			zeppelin.updateMostRecentAngle(angle);
		}
		else
		{
			// geen zinnige informatie te halen uit enkel één marker
			return;
		}
		GridPoint newPosition = calculatePosition(angle, foundMarkers);
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
		GridMarker originMarker;
		GridMarker otherMarker;
		MarkerOrientation orientationAtZero = equivalentMarkers.get(0).getOrientation();
		MarkerOrientation orientationAtOne = equivalentMarkers.get(1).getOrientation();
		// Controleer welke van de zes gevallen zich voordoet (zie driehoekjes.png en driehoekjesdown.png
		// in AangepastTussentijdsVerslag-sem2 in de Dropbox)
		
		// geval 1a
		if (orientationAtZero == MarkerOrientation.LEFT && orientationAtOne == MarkerOrientation.RIGHT)
		{
			originMarker = foundMarkers.get(foundMarkers.indexOf(equivalentMarkers.get(0)));
			otherMarker = foundMarkers.get(foundMarkers.indexOf(equivalentMarkers.get(1)));
			GridPoint originPoint = originMarker.getPoint();
			GridPoint otherPoint = otherMarker.getPoint();
			double newLeftY =  - (originPoint.y - image.getHeight());
			double newRightY = - (otherPoint.y - image.getHeight());
			
			GridPoint rightProjection = new GridPoint(otherPoint.x - originPoint.x, newRightY - newLeftY);
			return angleWithOffset(rightProjection.y, rightProjection.x, 0);
		}
		// geval 1a omgekeerd --> VOLGENS MIJ KAN JE HIER GEWOON + 180 DOEN TOV GEVAL 1A 
		if (orientationAtZero == MarkerOrientation.RIGHT && orientationAtOne == MarkerOrientation.LEFT)
		{
			originMarker = foundMarkers.get(foundMarkers.indexOf(equivalentMarkers.get(1)));
			otherMarker = foundMarkers.get(foundMarkers.indexOf(equivalentMarkers.get(0)));
			GridPoint originPoint = originMarker.getPoint();
			GridPoint otherPoint = otherMarker.getPoint();
			double newLeftY =  - (originPoint.y - image.getHeight());
			double newRightY = - (otherPoint.y - image.getHeight());
			
			GridPoint rightProjection = new GridPoint(otherPoint.x - originPoint.x, newRightY - newLeftY);
			return angleWithOffset(rightProjection.y, rightProjection.x, 0);
		}
		// geval 1b
		if (orientationAtZero == MarkerOrientation.UP && orientationAtOne == MarkerOrientation.LEFT)
		{
			originMarker = foundMarkers.get(foundMarkers.indexOf(equivalentMarkers.get(0)));
			otherMarker = foundMarkers.get(foundMarkers.indexOf(equivalentMarkers.get(1)));
			GridPoint originPoint = originMarker.getPoint();
			GridPoint otherPoint = otherMarker.getPoint();
			double newLeftY =  - (originPoint.y - image.getHeight());
			double newRightY = - (otherPoint.y - image.getHeight());
			
			GridPoint rightProjection = new GridPoint(otherPoint.x - originPoint.x, newRightY - newLeftY);
			return angleWithOffset(rightProjection.y, rightProjection.x, 120);
		}
		// geval 1b omgekeerd
		if (orientationAtZero == MarkerOrientation.LEFT && orientationAtOne == MarkerOrientation.UP)
		{
			originMarker = foundMarkers.get(foundMarkers.indexOf(equivalentMarkers.get(1)));
			otherMarker = foundMarkers.get(foundMarkers.indexOf(equivalentMarkers.get(0)));
			GridPoint originPoint = originMarker.getPoint();
			GridPoint otherPoint = otherMarker.getPoint();
			double newLeftY =  - (originPoint.y - image.getHeight());
			double newRightY = - (otherPoint.y - image.getHeight());
			
			GridPoint rightProjection = new GridPoint(otherPoint.x - originPoint.x, newRightY - newLeftY);
			return angleWithOffset(rightProjection.y, rightProjection.x, 120);
		}
		// geval 1c
		if (orientationAtZero == MarkerOrientation.UP && orientationAtOne == MarkerOrientation.RIGHT)
		{
			originMarker = foundMarkers.get(foundMarkers.indexOf(equivalentMarkers.get(0)));
			otherMarker = foundMarkers.get(foundMarkers.indexOf(equivalentMarkers.get(1)));
			GridPoint originPoint = originMarker.getPoint();
			GridPoint otherPoint = otherMarker.getPoint();
			double newLeftY =  - (originPoint.y - image.getHeight());
			double newRightY = - (otherPoint.y - image.getHeight());
			
			GridPoint rightProjection = new GridPoint(otherPoint.x - originPoint.x, newRightY - newLeftY);
			return angleWithOffset(rightProjection.y, rightProjection.x, 60);
		}
		// geval 1c omgekeerd
		if (orientationAtZero == MarkerOrientation.RIGHT && orientationAtOne == MarkerOrientation.UP)
		{
			originMarker = foundMarkers.get(foundMarkers.indexOf(equivalentMarkers.get(1)));
			otherMarker = foundMarkers.get(foundMarkers.indexOf(equivalentMarkers.get(0)));
			GridPoint originPoint = originMarker.getPoint();
			GridPoint otherPoint = otherMarker.getPoint();
			double newLeftY =  - (originPoint.y - image.getHeight());
			double newRightY = - (otherPoint.y - image.getHeight());
			
			GridPoint rightProjection = new GridPoint(otherPoint.x - originPoint.x, newRightY - newLeftY);
			return angleWithOffset(rightProjection.y, rightProjection.x, 60);
		}
		// geval 2a
		if (orientationAtZero == MarkerOrientation.DOWN && orientationAtOne == MarkerOrientation.LEFT)
		{
			originMarker = foundMarkers.get(foundMarkers.indexOf(equivalentMarkers.get(0)));
			otherMarker = foundMarkers.get(foundMarkers.indexOf(equivalentMarkers.get(1)));
			GridPoint originPoint = originMarker.getPoint();
			GridPoint otherPoint = otherMarker.getPoint();
			double newLeftY =  - (originPoint.y - image.getHeight());
			double newRightY = - (otherPoint.y - image.getHeight());
			
			GridPoint rightProjection = new GridPoint(otherPoint.x - originPoint.x, newRightY - newLeftY);
			return angleWithOffset(rightProjection.y, rightProjection.x, 240);
		}
		// geval 2a omgekeerd
		if (orientationAtZero == MarkerOrientation.LEFT && orientationAtOne == MarkerOrientation.DOWN)
		{
			originMarker = foundMarkers.get(foundMarkers.indexOf(equivalentMarkers.get(1)));
			otherMarker = foundMarkers.get(foundMarkers.indexOf(equivalentMarkers.get(0)));
			GridPoint originPoint = originMarker.getPoint();
			GridPoint otherPoint = otherMarker.getPoint();
			double newLeftY =  - (originPoint.y - image.getHeight());
			double newRightY = - (otherPoint.y - image.getHeight());
			
			GridPoint rightProjection = new GridPoint(otherPoint.x - originPoint.x, newRightY - newLeftY);
			return angleWithOffset(rightProjection.y, rightProjection.x, 240);
		}
		// geval 2b
		if (orientationAtZero == MarkerOrientation.DOWN && orientationAtOne == MarkerOrientation.RIGHT)
		{
			originMarker = foundMarkers.get(foundMarkers.indexOf(equivalentMarkers.get(0)));
			otherMarker = foundMarkers.get(foundMarkers.indexOf(equivalentMarkers.get(1)));
			GridPoint originPoint = originMarker.getPoint();
			GridPoint otherPoint = otherMarker.getPoint();
			double newLeftY =  - (originPoint.y - image.getHeight());
			double newRightY = - (otherPoint.y - image.getHeight());
			
			GridPoint rightProjection = new GridPoint(otherPoint.x - originPoint.x, newRightY - newLeftY);
			return angleWithOffset(rightProjection.y, rightProjection.x, 300);
		}
		// geval 2b omgekeerd
		else // (oneOr == MarkerOrientation.RIGHT && otherOr == MarkerOrientation.DOWN)
		{
			originMarker = foundMarkers.get(foundMarkers.indexOf(equivalentMarkers.get(1)));
			otherMarker = foundMarkers.get(foundMarkers.indexOf(equivalentMarkers.get(0)));
			GridPoint originPoint = originMarker.getPoint();
			GridPoint otherPoint = otherMarker.getPoint();
			double newLeftY =  - (originPoint.y - image.getHeight());
			double newRightY = - (otherPoint.y - image.getHeight());
			
			GridPoint rightProjection = new GridPoint(otherPoint.x - originPoint.x, newRightY - newLeftY);
			return angleWithOffset(rightProjection.y, rightProjection.x, 300);
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
	
	private GridPoint calculatePosition(double angle, List<GridMarker> foundMarkers)
	{
		double pixelLength = foundMarkers.get(0).getPoint().distanceTo(foundMarkers.get(1).getPoint());
		pixelLength = pixelLength/triangleSideLength;
		
		
	}
	
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
