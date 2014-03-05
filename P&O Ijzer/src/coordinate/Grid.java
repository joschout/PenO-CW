package coordinate;

import java.util.List;

public class Grid {
	
	public Grid(List<GridTriangle> gridTriangles)
	{
		this.gridTriangles = gridTriangles;
	}
	
	private List<GridTriangle> gridTriangles;
	
	public List<GridTriangle> getGridTriangles()
	{
		return this.gridTriangles;
	}
	
	public GridTriangle getMatchingTriangle(List<GridMarker> markers)
	{
		for (GridTriangle gridTriangle : getGridTriangles())
		{
			if (gridTriangle.markersMatch(markers))
			{
				return gridTriangle;
			}
		}
		return null;
	}

}
