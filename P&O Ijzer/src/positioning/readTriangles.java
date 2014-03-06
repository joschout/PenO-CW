package positioning;

import java.util.ArrayList;
import java.util.List;

import coordinate.GridMarker;

public class readTriangles {
	
	List<GridMarker> markers;
	List<Couple> couples;

	public void getFiguresImage() {	
		markers = getMarkers();		
	}
	
	public void getTriangles() {
		getCouples();
		// koppels linken en naar driehoeken brengen TODO
	}
	
	public void getCouples() {
		double pixellength = getPixellengthTriangle();
		for(GridMarker marker1: markers) {
			for(GridMarker marker2: markers) {
				if(getDistance(marker1, marker2) < pixellength*1.15) { // 15% foutmarge
					couples.add(new Couple(marker1, marker2));   //koppel dat bij elkaar hoort in kalsse Couple steken.
				}
			}
		}
	}
	
	public double getPixellengthTriangle() {
		GridMarker firstMarker = markers.get(0);
		double smallestDistance = image.getHeight();
		for(int i=1; i < markers.size(); i++) {
			double distance = getDistance(firstMarker, markers.get(i));
			if(distance < smallestDistance) {
				smallestDistance = distance;
			}
		}
		return smallestDistance; //aantal pixels dat 40cm in het echt voorstel (ongeveer)
			
	
	}

	private double getDistance(GridMarker firstMarker, GridMarker gridMarker) {
		// TODO  NOG DISTANCE BEPALEN: VECTOR REKENEN.
		return 0;
	}
	
	
}


