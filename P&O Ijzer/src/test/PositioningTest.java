package test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import org.opencv.core.Core;

import positioning.AngleCalculator;
import positioning.Couple;
import positioning.CoupleTriangleMatcher;
import positioning.Image;
import positioning.PositionCalculator;
import positioning.ReadCouples;
import controllers.CameraController;
import coordinate.Grid;
import coordinate.GridInitialiser;
import coordinate.GridMarker;
import coordinate.GridPoint;
import coordinate.GridTriangle;
import zeppelin.MainProgramImpl;

public class PositioningTest {

	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		GridInitialiser init = new GridInitialiser();
		CameraController cam = new CameraController();
		FileWriter fstream = null;
		BufferedWriter out = null;
		PrintWriter writer = null;
		File file = new File("/home/pi/test-" + Long.toString(System.currentTimeMillis()));
		try {
			fstream = new FileWriter(file, true);
			out = new BufferedWriter(fstream);
			writer = new PrintWriter(out);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			Grid grid = init.readGrid("grid");
			boolean exit = false;
			System.out.println("Enter q to quit");
			int fileCounter = 0;
			while (! exit) {
				if (fileCounter > 10) {
					fileCounter = 0;
				}
				String filename = "foto-" + Integer.toString(fileCounter);
				fileCounter++;
				Image image = cam.takePicture(filename);
				ReadCouples readCouples = null;
				GridTriangle triangle = null;
				double angle = 0;
				GridPoint position = null;
				StringBuilder builder;
				try {
					readCouples = new ReadCouples(image);
					triangle = triangleMatch(grid, image, readCouples);

					Couple pictureCouple = null;
					Couple triangleCouple = null;

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

					AngleCalculator calc = new AngleCalculator(image, pictureCouple, triangleCouple);
					angle = calc.calculateAngle();
					PositionCalculator calcPos = new PositionCalculator(image, pictureCouple, triangleCouple);
					position = calcPos.calculatePosition(angle);
				} catch (Exception e) {
					System.out.println("=== WAARSCHUWING: exception ===\n");
					builder = new StringBuilder();
					builder.append("--- Gevonden markers ---\n");
					for (GridMarker marker : image.getMarkers()) {
						builder.append("Marker: " + marker.toString() + "\n");
					}
					builder.append("--- Gevonden couples ---\n");
					for (Couple couple : readCouples.getListCouples()) {
						builder.append("Couple: " + couple.toString() + "\n");
					}
					builder.append("--- Gevonden driehoek ---\n");
					builder.append("Driehoek: " + triangle.toString() + "\n");
					e.printStackTrace();
					System.out.println(builder.toString());
					writer.write("=== WAARSCHUWING: exception ===\n");
					e.printStackTrace(writer);
					writer.write(builder.toString());
					continue;
				}
				builder = new StringBuilder();
				builder.append("=== NIEUWE ITERATIE ===\n");
				builder.append("--- Resultaten ---\n");
				builder.append("Foto: " + filename + "\n");
				builder.append("Hoek: " + angle + "\n");
				builder.append("Positie: " + position.toString() + "\n");
				builder.append("--- Gevonden markers ---\n");
				for (GridMarker marker : image.getMarkers()) {
					builder.append("Marker: " + marker.toString() + "\n");
				}
				builder.append("--- Gevonden couples ---\n");
				for (Couple couple : readCouples.getListCouples()) {
					builder.append("Couple: " + couple.toString() + "\n");
				}
				builder.append("--- Gevonden driehoek ---\n");
				builder.append("Driehoek: " + triangle.toString() + "\n");
				builder.append("=== EINDE ITERATIE ===\n");
				String toWrite = builder.toString();
				System.out.println(toWrite);
				writer.write(toWrite);
				
				int available = System.in.available();
				if (available > 0) {
					byte[] input = new byte[1];
					System.in.read(input, 0 , 1);
					String character = new String(input);
					if (character.equals("q")) {
						exit = true;
					}
				}
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
			writer.close();
			
		} catch (InterruptedException e) {
			e.printStackTrace();
			writer.close();
		}
		

	}
	
	private static GridTriangle triangleMatch(Grid grid, Image image, ReadCouples readCouples) throws IOException, InterruptedException {
		CoupleTriangleMatcher matcher = new CoupleTriangleMatcher(grid, readCouples, new GridPoint(0,0));
		GridTriangle triangle = matcher.matchCouplesWithTriangles();
		return triangle;
	}

}