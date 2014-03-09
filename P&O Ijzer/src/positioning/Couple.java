package positioning;

import java.util.ArrayList;
import java.util.List;

import coordinate.GridMarker;

public class Couple {
	
	private static final double SMALL_TRIANGLE = 0; //TODO pixel bepalen voor klein opp
	private GridMarker marker1;
	private GridMarker marker2;

	public Couple(GridMarker marker1, GridMarker marker2) {
		this.marker1 = marker1;
		this.marker2 = marker2;
	}
	
	public GridMarker getMarker1() {
		return marker1;
	}

	public GridMarker getMarker2() {
		return marker2;
	}
	
	public boolean contains(GridMarker marker1, GridMarker marker2) {
		if(this.marker1 == marker1 || this.marker1 == marker2) {
			if(this.marker2 == marker1 || this.marker2 == marker2) {
				return true;
			}
		}
		
		return false;
	}
	
	public boolean formTriangle(Couple couple) {
		GridMarker marker1 = couple.getMarker1();
		GridMarker marker2 = couple.getMarker2();
		if(contains(marker1, marker2)) { // als ze hetzelfde zijn -> false
			return false;
		}
		if(this.marker1 == marker1 || this.marker1 == marker2 || this.marker2 == marker1 || this.marker2 == marker2) {
			if(!formLine(getNonRedundantMarkers(this.marker1, this.marker2, marker1, marker2))) {
				return true;
			}
		}
		return false;
	}
	
	public List<GridMarker> getNonRedundantMarkers(GridMarker marker1, GridMarker marker2, GridMarker marker3, GridMarker marker4) {
		List<GridMarker> markers = new ArrayList<GridMarker>();
		if(!(marker1 == marker2 || marker1 == marker3 || marker1 == marker4)) {
			markers.add(marker1);
		}
		if(!(marker2 == marker1 || marker2 == marker3 || marker2 == marker4)) {
			markers.add(marker2);
		}
		if(!(marker3 == marker2 || marker3 == marker1 || marker3 == marker4)) {
			markers.add(marker3);
		}
		if(!(marker4 == marker2 || marker4 == marker3 || marker4 == marker1)) {
			markers.add(marker4);
		}
		return markers;
			
	}

	//check op driehoek oppervlak
	private boolean formLine(List<GridMarker> markers) {
		if(markers.get(0).getPoint().x * (markers.get(1).getPoint().y - markers.get(2).getPoint().y) +
				markers.get(1).getPoint().x * (markers.get(2).getPoint().y - markers.get(0).getPoint().y) + 
					markers.get(2).getPoint().x * (markers.get(0).getPoint().y - markers.get(1).getPoint().y) < SMALL_TRIANGLE) {
			return true;
		}
		return false;
	}
	
	
}
