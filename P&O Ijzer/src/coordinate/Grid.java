package coordinate;

import java.util.List;

public class Grid {
	
	public Grid(List<GridTriangle> gridTriangles, List<Tablet> tablets)
	{
		this.gridTriangles = gridTriangles;
		this.tablets = tablets;
	}
	
	private List<Tablet> tablets;
	
//	private Map<Integer,Tablet> tablets;
	
	public List<Tablet> getTablets()
	{
		return this.tablets;
	}
	
	private List<GridTriangle> gridTriangles;
	
	public List<GridTriangle> getGridTriangles()
	{
		return this.gridTriangles;
	}
	
	/**
	 * Vindt de driehoek in het rooster dat het beste overeenkomt met de gegeven
	 * markeerpunten, waarbij rekening wordt gehouden met de gegeven positie.
	 * @param markers
	 * 		Markeerpunten gevonden in een foto.
	 * @param oldPosition
	 * 		De meest recente positie van de zeppelin.
	 * @return
	 * 		De gegeven driehoek beantwoordt aan de volgende eisen:
	 * 			de driehoek bevat zoveel mogelijk van de gegeven markeerpunten
	 * 			(met natuurlijk een maximum van drie) en het centrum van de driehoek
	 * 			ligt dichter bij de oude positie dan van andere driehoeken die even
	 * 			veel overeenkomende punten hebben.
	 */
	public GridTriangle getBestMatch(List<GridMarker> markers, GridPoint oldPosition)
	{
		int matchCount = 0;
		GridTriangle bestMatch = null;
		double distanceToBestMatch = Double.POSITIVE_INFINITY;
		for (GridTriangle gridTriangle : getGridTriangles())
		{
			int triangleMatches = gridTriangle.countMatchingMarkers(markers);
			double distanceToTriangle = gridTriangle.distanceToCenter(oldPosition);
			if (triangleMatches > matchCount)
			{
				matchCount = triangleMatches;
				bestMatch = gridTriangle;
				distanceToBestMatch = distanceToTriangle;
			}
			else if (gridTriangle.distanceToCenter(oldPosition) < distanceToBestMatch)
			{
				bestMatch = gridTriangle;
				distanceToBestMatch = distanceToTriangle;
			}
		}
		return bestMatch;
	}
	
	@Override
	public String toString()
	{
		StringBuilder toReturn = new StringBuilder();
		toReturn.append("=== GRID DUMP ===\n");
		for (GridTriangle triangle: this.getGridTriangles())
		{
			toReturn.append(triangle + "\n");
		}
		toReturn.append("=== END GRID DUMP ===");
		return toReturn.toString();
	}
	
	public Tablet getTabletWithTabletId(int id){
		for(Tablet tab: getTablets()){
			if (tab.getId() == id){
				return tab;
			}
		}
		return null;
	}

}
