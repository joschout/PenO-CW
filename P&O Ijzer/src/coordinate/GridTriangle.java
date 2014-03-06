package coordinate;

import java.util.ArrayList;
import java.util.List;

public class GridTriangle {
	
	/**
	 * 
	 * @param gridMarkers 
	 * 		Er wordt verondersteld dat het linkerpunt eerst komt, het rechterpunt tweede
	 *  		en het bovenste/onderste punt laatst.
	 * 		
	 */
	public GridTriangle(List<GridMarker> gridMarkers)
	{
		this.gridMarkers = gridMarkers;
	}
	
	private List<GridMarker> gridMarkers;
	
	public List<GridMarker> getGridMarkers()
	{
		return gridMarkers;
	}
	
	public int countMatchingMarkers(List<GridMarker> markers)
	{
		List<GridMarker> copyOfGridMarkers = new ArrayList<GridMarker>(gridMarkers);
		int matches = 0;
		for (GridMarker pictureMarker : markers)
		{
			if (copyOfGridMarkers.contains(pictureMarker))
			{
				matches++;
				copyOfGridMarkers.remove(pictureMarker);
			}
		}
		return matches;
	}
	
	/**
	 * Geeft de equivalenten van de GridMarkers in de gegeven lijst.
	 * @param markers
	 * 		Markers gevonden in een foto.
	 * @return De equivalenten van de gegeven markers, als die bestaan voor deze driehoek.
	 * 		Als er geen equivalenten bestaan wordt een lege lijst teruggegeven.
	 */
	public List<GridMarker> getEquivalentMarkers(List<GridMarker> markers)
	{
		List<GridMarker> copyOfPictureMarkers = new ArrayList<GridMarker>(markers);
		List<GridMarker> toReturn = new ArrayList<GridMarker>();
		for (GridMarker gridMarker : gridMarkers)
		{
			if (copyOfPictureMarkers.contains(gridMarker))
			{
				toReturn.add(gridMarker);
				copyOfPictureMarkers.remove(gridMarker);
			}
		}
		return toReturn;
	}
	
	/**
	 * Berekent de afstand van het gegeven punt tot het centrum van deze driehoek.
	 * @param point
	 * 		Punt waarvan de coördinaten zijn uitgedrukt in termen van het rooster.
	 * @return Afstand tussen gegeven punt en centrum van deze driehoek.
	 */
	public double distanceToCenter(GridPoint point)
	{
		GridPoint centroid = findCentroid();
		return centroid.distanceTo(point);
	}
	
	/**
	 * Berekent het zwaartepunt (equivalent met het centrum, want de driehoek is gelijkzijdig)
	 * van deze driehoek.
	 * @return Het zwaartepunt
	 */
	public GridPoint findCentroid()
	{
		GridPoint midpoint = gridMarkers.get(0).findMidpointBetween(gridMarkers.get(1));
		GridPoint oppositePoint = gridMarkers.get(2).getPoint();
		double xComponent = oppositePoint.x + (2/3) * (midpoint.x - oppositePoint.x);
		double yComponent = oppositePoint.y + (2/3) * (midpoint.y - oppositePoint.y);
		return new GridPoint(xComponent, yComponent);
	}

}
