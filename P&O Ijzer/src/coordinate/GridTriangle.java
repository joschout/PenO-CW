package coordinate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import positioning.Couple;

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
		this.centroid = this.findCentroid();
		this.initialiseCouples();
	}
	
	private List<GridMarker> gridMarkers;
	
	public List<GridMarker> getGridMarkers()
	{
		return gridMarkers;
	}
	
	private List<Couple> markerCouples;
	
	private void initialiseCouples()
	{
		markerCouples = new ArrayList<Couple>();
		for (int i = 0; i < this.gridMarkers.size(); i++)
		{
			for (int j = i+1; j < this.gridMarkers.size(); j++)
			{
				GridMarker marker1 = this.gridMarkers.get(i);
				GridMarker marker2 = this.gridMarkers.get(j);
				markerCouples.add(new Couple(marker1, marker2));
			}
		}
	}
	
	public int countMatchingCouples(List<Couple> pictureCouples)
	{
		List<Couple> copyOfTriangleCouples = new ArrayList<Couple>(markerCouples);
		int matches = 0;
		for (Couple pictureCouple : pictureCouples)
		{
			if (copyOfTriangleCouples.contains(pictureCouple))
			{
				matches++;
				copyOfTriangleCouples.remove(pictureCouple);
			}
		}
		return matches;
	}
	
	public Couple getMatchingCouple(Couple pictureCouple, boolean colorMatch)
	{
		for (Couple triangleCouple : markerCouples)
		{
			if (triangleCouple.equalsDispatch(pictureCouple, colorMatch))
			{
				return triangleCouple;
			}
		}
		return null;
	}
	
	public Couple getMatchingCoupleWithOrientation(Couple pictureCouple)
	{
		for (Couple triangleCouple : markerCouples)
		{
			if (triangleCouple.equalsWithOrientation(pictureCouple))
			{
				return triangleCouple;
			}
		}
		return null;
	}
	
	public List<MarkerOrientation> allOrientationsOf(GridMarker marker)
	{
		List<MarkerOrientation> toReturn = new ArrayList<MarkerOrientation>();
		for (GridMarker triangleMarker : this.getGridMarkers())
		{
			if (triangleMarker.equals(marker))
			{
				toReturn.add(triangleMarker.getOrientation());
			}
		}
		return toReturn;
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
	
	private GridPoint centroid;
	
	/**
	 * Berekent de afstand van het gegeven punt tot het centrum van deze driehoek.
	 * @param point
	 * 		Punt waarvan de coördinaten zijn uitgedrukt in termen van het rooster.
	 * @return Afstand tussen gegeven punt en centrum van deze driehoek.
	 */
	public double distanceToCenter(GridPoint point)
	{
		return this.getCentroid().distanceTo(point);
	}
	
	public GridPoint getCentroid()
	{
		return this.centroid;
	}
	
	/**
	 * Berekent het zwaartepunt (equivalent met het centrum, want de driehoek is gelijkzijdig)
	 * van deze driehoek.
	 * @return Het zwaartepunt
	 */
	private GridPoint findCentroid()
	{
		GridPoint midpoint = gridMarkers.get(0).findMidpointBetween(gridMarkers.get(1));
		GridPoint oppositePoint = gridMarkers.get(2).getPoint();
		double xComponent = oppositePoint.x + (2/3) * (midpoint.x - oppositePoint.x);
		double yComponent = oppositePoint.y + (2/3) * (midpoint.y - oppositePoint.y);
		return new GridPoint(xComponent, yComponent);
	}
	
	public String toString()
	{
		StringBuilder toReturn = new StringBuilder();
		toReturn.append("Triangle: ");
		for (GridMarker marker: this.getGridMarkers())
		{
			toReturn.append(marker + " " + marker.getOrientation() + "; ");
		}
		return toReturn.toString();
	}

	public int countColorMatchingCouples(List<Couple> pictureCouples) {
		List<Couple> copyOfTriangleCouples = new ArrayList<Couple>(markerCouples);
		int matches = 0;
		for (Couple pictureCouple : pictureCouples)
		{
			Iterator<Couple> iter = copyOfTriangleCouples.iterator();
			while (iter.hasNext()) {
				Couple next = iter.next();
				if (next.matchColor(pictureCouple)) {
					matches++;
					iter.remove();
				}
			}
//			for (Couple gridCouple: copyOfTriangleCouples) {
//				if(gridCouple.matchColor(pictureCouple))
//					matches++;
//					copyOfTriangleCouples.remove(pictureCouple);
//				
//			}
		}
		return matches;
	}
	
	private boolean mustMatchOnColor;
	
	public boolean getMustMatchOnColor() {
		return this.mustMatchOnColor;
	}
	
	public void setMustMatchOnColor(boolean bool) {
		this.mustMatchOnColor = bool;
	}

}
