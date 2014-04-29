package test;

import java.io.IOException;
import java.util.List;

import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

import positioning.AngleCalculator;
import positioning.Couple;
import positioning.CoupleTriangleMatcher;
import positioning.Image;
import positioning.ImageAnalyser;
import positioning.PositionCalculator;
import positioning.ReadCouples;
import controllers.CameraController;
import coordinate.Grid;
import coordinate.GridInitialiser;
import coordinate.GridMarker;
import coordinate.GridPoint;
import coordinate.GridTriangle;
import coordinate.MarkerOrientation;



public class Test {

	CameraController camera = new CameraController();

	public Test() throws InterruptedException, IOException {

		GridInitialiser gridInit = new GridInitialiser();
		Grid grid = gridInit.readGrid("grid");
		System.out.println(grid);
		Image image = takePictureRam("test.jpg");
		System.out.println("=== GEZIENE MARKERS ===");
		for (GridMarker marker: image.getMarkers())
		{
			System.out.println(marker.toString());
		}
		System.out.println("=== EINDE GEZIENE MARKERS ===");
		ReadCouples readCouples = new ReadCouples(image);
		GridTriangle triangle = triangleMatch(grid, image, readCouples);
		Couple pictureCouple = null;
		Couple triangleCouple = null;
		System.out.println("=== GEKOZEN DRIEHOEK ===");
		for (GridMarker marker : triangle.getGridMarkers())
		{
			System.out.println(marker.toString());
		}
		System.out.println("=== EINDE GEKOZEN DRIEHOEK ===");
		for (Couple pictureCoupleFor : readCouples.getListCouples())
		{
			Couple triangleCoupleFor = triangle.getMatchingCouple(pictureCoupleFor, triangle.getMustMatchOnColor());
			if (triangleCoupleFor == null)
			{
				continue;
			}
			pictureCouple = pictureCoupleFor;
			triangleCouple = triangleCoupleFor;
			break;
		}
		GridMarker marker1 = pictureCouple.getMarker1();
		GridMarker marker2 = pictureCouple.getMarker2();
		List<MarkerOrientation> marker1Orientations = triangle.allOrientationsOf(marker1);
		List<MarkerOrientation> marker2Orientations = triangle.allOrientationsOf(marker2);
		marker1.setOrientation(marker1Orientations.get(0));
		marker2.setOrientation(marker2Orientations.get(0));
		triangleCouple = triangle.getMatchingCoupleWithOrientation(pictureCouple);
		System.out.println("Picture couple: " + pictureCouple);
		System.out.println("Triangle couple: " + triangleCouple);
		AngleCalculator calc = new AngleCalculator(image, pictureCouple, triangleCouple);
		double angle = calc.calculateAngle();
		System.out.println("Angle: " + angle);
		PositionCalculator calcPos = new PositionCalculator(image, pictureCouple, triangleCouple);
		GridPoint position = calcPos.calculatePosition(angle);
		System.out.println("X: " + position.x + ", Y: " + position.y);
	}
	
	
	private GridTriangle triangleMatch(Grid grid, Image image, ReadCouples readCouples) throws IOException, InterruptedException {
		CoupleTriangleMatcher matcher = new CoupleTriangleMatcher(grid, readCouples, new GridPoint(0,0));
		GridTriangle triangle = matcher.matchCouplesWithTriangles();
		return triangle;
	}
	
	
	
	private List<Couple> readCouples() throws InterruptedException, IOException {
		Image image = takePictureRam("test");
		ReadCouples readCouples = new ReadCouples(image);
		readCouples.getCouples();
		return readCouples.getListCouples();
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













	public Image takePictureRam(String pFileName) throws InterruptedException, IOException {
		Image image = camera.takePicture(pFileName);		
		return image;
	}
	


	public void takePictureNormal(String pFileName) throws InterruptedException, IOException {
		executeShellCommand("raspistill -t 1 -w " + 500 + " -h "
				+ 500 + " -o " + "/home/pi/" + pFileName + ".jpg");

		//return new Image(Highgui.imread("/home/pi/" + pFileName + ".jpg"));
	}


	private void executeShellCommand(String pCommand) throws InterruptedException, IOException  
	{   
		Runtime run = Runtime.getRuntime() ;  
		Process pr = run.exec(pCommand) ;  
		pr.waitFor() ;  
	}
	
//	GridInitialiser gridInit = new GridInitialiser();
//	Grid grid = gridInit.readGrid("field");
//	Image image = takePictureRam("test");
//	ReadCouples readCouples = new ReadCouples(image);
//	GridTriangle triangle = triangleMatch(grid, image, readCouples);
//	Couple pictureCouple = null;
//	Couple triangleCouple = null;
//	for (Couple pictureCoupleFor : readCouples.getListCouples())
//	{
//		Couple triangleCoupleFor = triangle.getMatchingCouple(pictureCoupleFor);
//		if (triangleCoupleFor == null)
//		{
//			continue;
//		}
//		pictureCouple = pictureCoupleFor;
//		triangleCouple = triangleCoupleFor;
//		break;
//	}
//	AngleCalculator calc = new AngleCalculator(image, pictureCouple, triangleCouple);
//	System.out.println("Angle: " + (360 - calc.calculateAngle()));

}
