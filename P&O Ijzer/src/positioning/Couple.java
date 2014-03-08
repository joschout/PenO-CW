package positioning;

import coordinate.GridMarker;

public class Couple {
	
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
			if(!formLine(this.marker1, this.marker2, marker1, marker2)) {
				return true;
			}
		}
		return false;
	}

	//TODO CHECKEN WELKE GRIDMARK REDUNDANT IS -> OVERIGE 3 CHECKEN OP DRIEHOEKOPP.
	private boolean formLine(GridMarker marker1, GridMarker marker2, GridMarker marker3, GridMarker marker4) {
		Ax * (By - Cy) + Bx * (Cy - Ay) + Cx * (Ay - By)
		return false;
	}
	
	
}
