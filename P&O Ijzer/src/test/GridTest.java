package test;

import java.io.IOException;

import coordinate.Grid;
import coordinate.GridInitialiser;
import coordinate.GridTriangle;
import coordinate.Tablet;

public class GridTest {

	public static void main(String[] args) throws IOException {
		GridInitialiser init = new GridInitialiser();
		Grid grid = init.readGrid("C:/Users/Milan/Desktop/School/Dropbox/PO - computerwetenschappen/Grid/newGrid2804");
		
		for(GridTriangle triangle: grid.getGridTriangles()) {
			System.out.println(triangle.toString());
		}
		
		for(Tablet tablet: grid.getTablets() ) {
			System.out.println(tablet.toString());
		}
	}

}
