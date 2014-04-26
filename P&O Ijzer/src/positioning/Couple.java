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
		return (this.marker1.getPoint().equals(marker1.getPoint()) && this.marker2.getPoint().equals(marker2.getPoint()))
				|| (this.marker1.getPoint().equals(marker2.getPoint()) && this.marker2.getPoint().equals(marker1.getPoint()));
//		if(this.marker1.equals(marker1) || this.marker1.equals(marker2)) {
//			if(this.marker2.equals(marker1) || this.marker2.equals(marker2)) {
//				return true;
//			}
//		}
//		return false;
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
	
	public boolean equalsWithOrientation(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Couple other = (Couple) obj;
		if (this.marker1.equalsWithOrientation(other.marker1) && this.marker2.equalsWithOrientation(other.marker2)) {
			return true;
		}
		if (this.marker1.equalsWithOrientation(other.marker2) && this.marker2.equalsWithOrientation(other.marker1)) {
			return true;
		}
		return false;
	}
	
	@Override
	public String toString() {
		return "Marker 1: " + getMarker1().toString() + ", Marker 2: " + getMarker2().toString();
	}

	public boolean matchColor(Couple other) {
		if (this.marker1.matchColor(other.marker1) && this.marker2.matchColor(other.marker2)) {
			return true;
		}
		if (this.marker1.matchColor(other.marker2) && this.marker2.matchColor(other.marker1)) {
			return true;
		}
		return false;
	}
	
	
}
