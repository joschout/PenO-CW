package positioning;

import java.util.ArrayList;
import java.util.List;

import coordinate.GridMarker;
import coordinate.GridTriangle;

public class ReadCouples {
	
	private List<GridMarker> markers;
	private List<Couple> couples = new ArrayList<Couple>();
	//private List<GridTriangle> triangles = new ArrayList<GridTriangle>();
	private Image image;
	
	public ReadCouples(Image image) {
		this.image = image;
		getFiguresImage();
		getCouples();
	}

	public void getFiguresImage() {	
		markers = image.getMarkers();
	}
	
	public List<Couple> getListCouples() {
		return couples;
	}
	
	/*
	public void getTriangles() {
		getCouples();
		for(Couple couple1: couples) {
			for(Couple couple2: couples) {
				if(couple1.formTriangle(couple2)) {
					addTriangle(couple1, couple2);
				}
			}
		}
	}
	*/
	
	/*
	//houdt geen rekening met left, right, up, down!
	private void addTriangle(Couple couple1, Couple couple2) {
		GridMarker marker1 = couple1.getMarker1();
		GridMarker marker2 = couple1.getMarker2();
		GridMarker marker3 = couple2.getMarker1();
		GridMarker marker4 = couple2.getMarker2();
		
		triangles.add(new GridTriangle(couple1.getNonRedundantMarkers(marker1, marker2, marker3, marker4)));
	}
	*/

	public void getCouples() {
		double pixellength = getPixellengthCouples();
		for(GridMarker marker1: markers) {
			for(GridMarker marker2: markers) {
				if((! areSameMarker(marker1, marker2)) && checkCouple(marker1, marker2) && getDistance(marker1, marker2) < pixellength*1.15) { // 15% foutmarge
					couples.add(new Couple(marker1, marker2));   //koppel dat bij elkaar hoort in klasse Couple steken.
				}
			}
		}
	}
	
	// Deelconditie bij getCouples: !marker1.equals(marker2)
	//  
	
	public double getPixellengthCouples() {
		GridMarker firstMarker = markers.get(0);
		double smallestDistance = image.getHeight();
		for(int i=1; i < markers.size(); i++) {
			double distance = getDistance(firstMarker, markers.get(i));
			if(distance < smallestDistance) {
				smallestDistance = distance;
			}
		}
		image.setPixelLength(smallestDistance / 40);
		return smallestDistance; //aantal pixels dat 40cm in het echt voorstel (ongeveer)
			
	
	}

	private double getDistance(GridMarker marker1, GridMarker marker2) {
		return marker1.getPoint().distanceTo(marker2.getPoint());
	}
	
	private boolean checkCouple(GridMarker marker1, GridMarker marker2) {
		for(Couple couple: couples) {
			if(couple.contains(marker1, marker2)) {
				return false;
			}
		}
		return true;
	}
	
	public List<GridMarker> getMarkers() {
		return markers;
	}
	
	private boolean areSameMarker(GridMarker marker1, GridMarker marker2)
	{
		return marker1.getPoint().equals(marker2.getPoint());
	}
}


