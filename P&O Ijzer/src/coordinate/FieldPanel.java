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

public	class FieldPanel extends JPanel {

	public double getTranslationX() {
		return translationX;
	}

	public void setTranslationX(double translationX) {
		this.translationX = translationX;
	}

	public double getTranslationY() {
		return translationY;
	}

	public void setTranslationY(double translationY) {
		this.translationY = translationY;
	}

	public double getScaleX() {
		return scaleX;
	}

	public void setScaleX(double scaleX) {
		this.scaleX = scaleX;
	}

	public double getScaleY() {
		return scaleY;
	}

	public void setScaleY(double scaleY) {
		this.scaleY = scaleY;
	}


	private double translationX;
	private double translationY;
	private double scaleX;
	private double scaleY;
	
	
	/////////////
	 private int frameRate = 5;
	/////////////
	public FieldPanel(double translationX, double translationY, double scaleX, double scaleY){
		setTranslationX(translationX);
		setTranslationY(translationY);
		setScaleX(scaleX);
		setScaleY(scaleY);
		setZeppelinMarker(new ZeppelinMarker(new GridPoint(40, 2*GridInitialiser.getMatrixDisplacementY())));
		
		GridInitialiser gritInitialiser = new GridInitialiser();
		try {
			setFieldGrid(gritInitialiser.readGrid("C:\\Users\\Jonas\\Desktop\\PenO\\gridTestFile2"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	//////////////////////	
		 Thread animationThread = new Thread () {
	         @Override
	         public void run() {
	            while (true) {
	               update();   // update the position and image
	               repaint();  // Refresh the display
	               try {
	                  Thread.sleep(1000 / frameRate); // delay and yield to other threads
	               } catch (InterruptedException ex) { }
	            }
	         }
	      };
	      animationThread.start(); 
	//////////////////////////////////	
		
	}

	
	
	/** Update the position based on speed and direction of the sprite */
	   public void update() {
		   
		  double x= getZeppelinMarker().getPoint().x;
		  double y= getZeppelinMarker().getPoint().y;
		  
		  x+=20;
		  y+=GridInitialiser.getMatrixDisplacementY();
		   GridPoint point = new GridPoint(x,y);
		   getZeppelinMarker().setPoint(point);
		   
		   
		   
		   
//	      x += speed * Math.cos(Math.toRadians(direction));  // x-position
//	      if (x >= CANVAS_WIDTH) {
//	         x -= CANVAS_WIDTH;
//	      } else if (x < 0) {
//	         x += CANVAS_WIDTH;
//	      }
//	      y += speed * Math.sin(Math.toRadians(direction));  // y-position
//	      if (y >= CANVAS_HEIGHT) {
//	         y -= CANVAS_HEIGHT;
//	      } else if (y < 0) {
//	         y += CANVAS_HEIGHT;
//	      }
//	      direction += rotationSpeed;  // update direction based on rotational speed
//	      if (direction >= 360) {
//	         direction -= 360;
//	      } else if (direction < 0) {
//	         direction += 360;
//	      }
//	      ++currentFrame;    // display next frame
//	      if (currentFrame >= imgFrames.length) {
//	         currentFrame = 0;
//	      }
	   }
	
	
	
	
	
	
	
	@Override
	public void paintComponent(Graphics g) {

		super.paintComponent(g);
		setBackground(Color.CYAN);
		doDrawing(g);
	}    

	private void doDrawing(Graphics g) {
	
		Graphics2D g2d = (Graphics2D) g;
		g2d.translate(getTranslationX(), getTranslationY());
		g2d.scale(getScaleX(), getScaleY());

		drawField(g2d);
		drawZeppelinMarker(g2d);
	}

	private void drawField(Graphics2D g2d){
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
	
	public ZeppelinMarker zeppelinMarker;
	
	public ZeppelinMarker getZeppelinMarker() {
		return zeppelinMarker;
	}

	public void setZeppelinMarker(ZeppelinMarker zeppelinMarker) {
		this.zeppelinMarker = zeppelinMarker;
	}

	public void drawZeppelinMarker(Graphics g){
		
		getZeppelinMarker().drawMarker(g);
	}
	
}
