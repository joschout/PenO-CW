package coordinate;

//http://www.zetcode.com/gfx/java2d/basicdrawing/
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;

public	class Field extends JPanel {

	public int getTranslationX() {
		return translationX;
	}

	public void setTranslationX(int translationX) {
		this.translationX = translationX;
	}

	public int getTranslationY() {
		return translationY;
	}

	public void setTranslationY(int translationY) {
		this.translationY = translationY;
	}

	public int getScaleX() {
		return scaleX;
	}

	public void setScaleX(int scaleX) {
		this.scaleX = scaleX;
	}

	public int getScaleY() {
		return scaleY;
	}

	public void setScaleY(int scaleY) {
		this.scaleY = scaleY;
	}


	private int translationX;
	private int translationY;
	private int scaleX;
	private int scaleY;
	
	
	
	public Field(int translationX, int translationY, int scaleX, int scaleY){
		setTranslationX(translationX);
		setTranslationY(translationY);
		setScaleX(scaleX);
		setScaleY(scaleY);
		GridInitialiser gritInitialiser = new GridInitialiser();
		try {
			setFieldGrid(gritInitialiser.readGrid("C:\\Users\\Jonas\\git\\PenOCW\\P&O Ijzer\\src\\grid"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void paintComponent(Graphics g) {

		super.paintComponent(g);
		doDrawing(g);
	}    

	private void doDrawing(Graphics g) {
	
		Graphics2D g2d = (Graphics2D) g;
		drawField(g2d);
		drawZeppelinMarker(g2d, 60,60);
	}

	private void drawField(Graphics2D g2d){

		g2d.translate(getTranslationX(), getTranslationY());
		g2d.scale(getScaleX(), getScaleY());
		List<GridTriangle> gridTriangleList = getFieldGrid().getGridTriangles();
		List<Polygon> trianglePolygonList = new ArrayList<Polygon>();
		List<GridMarker> allGridMarkers = new ArrayList<GridMarker>();

		for(GridTriangle tria: gridTriangleList){ 		

			List<GridMarker> gridMarkerList = tria.getGridMarkers();
			int[] x = new int[3];
			int[] y = new int[3];

			//voor elk van de 3 markers:
				for(int i=0; i<=2; i++){
					x[i] = (int) gridMarkerList.get(i).getPoint().x;
					y[i] = (int) gridMarkerList.get(i).getPoint().y;
					allGridMarkers.add(gridMarkerList.get(i));

					//gridMarkerList.get(i).drawMarker(g2d, x[i], y[i]);
				}

				Polygon trianglePolygon = new Polygon(x,y,3);
				trianglePolygonList.add(trianglePolygon);
				g2d.setColor(Color.black);
				g2d.drawPolygon(trianglePolygon);	
		}	 	
		for(GridMarker marker: allGridMarkers)
		{
			marker.drawMarker(g2d);
		}
	}


	private Grid fieldGrid;

	public Grid getFieldGrid() {
		return fieldGrid;
	}

	public void setFieldGrid(Grid fieldGrid) {
		this.fieldGrid = fieldGrid;
		
	}
	
	public void drawZeppelinMarker(Graphics g, int x, int y){
		ZeppelinMarker marker = new ZeppelinMarker(new GridPoint(x,y));
		marker.drawMarker(g, x, y);
	}
	
}
