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
		if(this.marker1.equals(marker1) || this.marker1.equals(marker2)) {
			if(this.marker2.equals(marker1) || this.marker2.equals(marker2)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((marker1 == null) ? 0 : marker1.hashCode());
		result = prime * result + ((marker2 == null) ? 0 : marker2.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Couple other = (Couple) obj;
		if (this.marker1.equals(other.marker1) && this.marker2.equals(other.marker2)) {
			return true;
		}
		if (this.marker1.equals(other.marker2) && this.marker2.equals(other.marker1)) {
			return true;
		}
		return false;
	}
	
	/*
	public boolean formTriangle(Couple couple) {
		GridMarker marker1 = couple.getMarker1();
		GridMarker marker2 = couple.getMarker2();
		if(contains(marker1, marker2)) { // als ze hetzelfde zijn -> false
			return false;
		}
		if(this.marker1.equals(marker1) || this.marker1.equals(marker2) || this.marker2.equals(marker1) || this.marker2.equals(marker2)) {
			if(!formLine(getNonRedundantMarkers(this.marker1, this.marker2, marker1, marker2))) {
				return true;
			}
		}
		return false;
	}
	*/
	
	/*
	public List<GridMarker> getNonRedundantMarkers(GridMarker marker1, GridMarker marker2, GridMarker marker3, GridMarker marker4) {
		List<GridMarker> markers = new ArrayList<GridMarker>();
		if(!(marker1.equals(marker2) || marker1.equals(marker3) || marker1.equals(marker4))) {
			markers.add(marker1);
		}
		if(!(marker2.equals(marker1) || marker2.equals(marker3) || marker2.equals(marker4))) {
			markers.add(marker2);
		}
		if(!(marker3.equals(marker2) || marker3.equals(marker1) || marker3.equals(marker4))) {
			markers.add(marker3);
		}
		if(!(marker4.equals(marker2) || marker4.equals(marker3) || marker4.equals(marker1))) {
			markers.add(marker4);
		}
		return markers;
			
	}
	*/

	
	/*
	//check op driehoek oppervlak
	private boolean formLine(List<GridMarker> markers) {
		if(markers.get(0).getPoint().x * (markers.get(1).getPoint().y - markers.get(2).getPoint().y) +
				markers.get(1).getPoint().x * (markers.get(2).getPoint().y - markers.get(0).getPoint().y) + 
					markers.get(2).getPoint().x * (markers.get(0).getPoint().y - markers.get(1).getPoint().y) < SMALL_TRIANGLE) {
			return true;
		}
		return false;
	}
	*/
	
	
}
