
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
		boolean mustMatchOnColor = false;
		
		for(GridTriangle triangle: triangles) {
			double distanceToCenter = triangle.distanceToCenter(recentPosition);
			int numMatches = triangle.countMatchingCouples(couples);
			if (numMatches > matchHeuristic)
			{
				matchHeuristic = numMatches;
				match = triangle;
				distanceHeuristic = distanceToCenter;
			}
			else if (numMatches == matchHeuristic)
			{
				if (match == null
					|| (! match.noDuplicateMarkers() && triangle.noDuplicateMarkers())
					|| distanceToCenter < distanceHeuristic) {
					match = triangle;
					distanceHeuristic = distanceToCenter;
				}
			}
			
		}
//		if(match.distanceToCenter(recentPosition) > 100) {
//			System.out.println("WAARSCHUWING: color matching");
//			System.out.println("Afstand: " + match.distanceToCenter(recentPosition));
////			distanceHeuristic = Double.POSITIVE_INFINITY;
////			double triangleScore = 0;
//			mustMatchOnColor = true;
//			for(GridTriangle triangle: triangles) {
//				int numMatches = triangle.countColorMatchingCouples(couples);
//				double distanceToCenter = triangle.distanceToCenter(recentPosition);
//				if (numMatches > matchHeuristic)
//				{
//					matchHeuristic = numMatches;
//					match = triangle;
//					distanceHeuristic = distanceToCenter;
//				}
//				else if (numMatches == matchHeuristic)
//				{
//					if ((! match.noDuplicateMarkers() && triangle.noDuplicateMarkers())
//							|| distanceToCenter < distanceHeuristic) {
//						match = triangle;
//						distanceHeuristic = distanceToCenter;
//					}
//				}
//				
//			}
//		}
		match.setMustMatchOnColor(mustMatchOnColor);
//		if(match.distanceToCenter(recentPosition) > 50) {
//			return null;
//		}
		return match;
	}
	
	
}
