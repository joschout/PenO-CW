package coordinate;

import java.util.List;

public class GridTriangle {
	
	public GridTriangle(List<GridMarker> gridMarkers)
	{
		this.gridMarkers = gridMarkers;
	}
	
	private List<GridMarker> gridMarkers;
	
	public List<GridMarker> getGridMarkers()
	{
		return gridMarkers;
	}
	
	public boolean markersMatch(List<GridMarker> markers)
	{
		return markers.containsAll(gridMarkers);
	}

}
