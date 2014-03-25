package positioning;

import java.util.ArrayList;
import java.util.List;

import coordinate.Grid;
import coordinate.GridPoint;
import coordinate.GridTriangle;

public class CoupleTriangleMatcher {

	List<GridTriangle> triangles;
	List<Couple> couples;
	GridPoint recentPosition;
	
	List<GridTriangle> matchedTriangles = new ArrayList<GridTriangle>();
	
	public CoupleTriangleMatcher(Grid grid, ReadCouples readCouples, GridPoint recentPosition) {
		triangles = grid.getGridTriangles();
		couples = readCouples.getListCouples();
		this.recentPosition = recentPosition;
	}
	
	public GridTriangle matchCouplesWithTriangles() {
		GridTriangle match = null;
		
		int matchHeuristic = 0;
		double distanceHeuristic = Double.POSITIVE_INFINITY;
		
		for(GridTriangle triangle: triangles) {
			double distanceToCenter = triangle.distanceToCenter(recentPosition);
			int numMatches = triangle.countMatchingCouples(couples);
			if (numMatches > matchHeuristic)
			{
				matchHeuristic = numMatches;
				match = triangle;
				distanceHeuristic = distanceToCenter;
			}
			else if (numMatches == matchHeuristic && distanceToCenter < distanceHeuristic)
			{
				match = triangle;
				distanceHeuristic = distanceToCenter;
			}
			
		}
		return match;
	}
	
	
}
