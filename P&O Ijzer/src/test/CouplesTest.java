package test;

import java.io.IOException;
import java.util.List;

import org.opencv.core.Core;

import positioning.Image;
import positioning.ImageAnalyser;
import controllers.CameraController;
import coordinate.GridMarker;

public class CouplesTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

	}
	
	CameraController camera = new CameraController();

	public Test() throws InterruptedException, IOException {
		getMarkersPicture();
	}
	
	
	
	
	
	
	private void getMarkersPicture() throws InterruptedException, IOException {
		Long startTime = System.currentTimeMillis();
		Image image = takePictureRam("test");
		ImageAnalyser analyser = new ImageAnalyser(image);
		List<GridMarker> markers = analyser.analysePicture();
		for(GridMarker marker: markers) {
			System.out.println("marker: " + marker.getShape() + ", " + marker.getColour());
		}
		
		Long endTime = System.currentTimeMillis();
		System.out.println("durationRam: " + (endTime - startTime));
		
	}

}
