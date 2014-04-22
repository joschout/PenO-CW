package coordinate;

//http://www.zetcode.com/gfx/java2d/basicdrawing/
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import RabbitMQ.RabbitMQController;
import RabbitMQ.RabbitMQControllerClient;

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

	
//	public RabbitMQControllerClient getRabbitMQControllerClient(){
//		return rabbitMQControllerClient;
//	}
//	
	private double translationX;
	private double translationY;
	private double scaleX;
	private double scaleY;
	private String absoluteGridFilePath;
//	private RabbitMQControllerClient rabbitMQControllerClient;
	
	/////////////
    private int frameRate = 5;
	/////////////
	public FieldPanel(double translationX, double translationY, double scaleX, double scaleY){
	//	this.rabbitMQControllerClient = rabbitMQControllerClient;
		setTranslationX(translationX);
		setTranslationY(translationY);
		setScaleX(scaleX);
		setScaleY(scaleY);
		setZeppelinMarkerList(new ArrayList<ZeppelinMarker>());
		getZeppelinMarkerList().add(new ZeppelinMarker(new GridPoint(40, 2*GridInitialiser.getMatrixDisplacementY())));

		this.direction = true;
		GridInitialiser gritInitialiser = new GridInitialiser();

		try {
			
			
		    JFileChooser chooser = new JFileChooser();
		    FileNameExtensionFilter filter = new FileNameExtensionFilter(
		        "CSV & TXT Images", "csv", "txt");
		    chooser.setFileFilter(filter);
		    int returnVal = chooser.showOpenDialog(getParent());
		    if(returnVal == JFileChooser.APPROVE_OPTION) {
		    setFieldGrid(gritInitialiser.readGrid(chooser.getSelectedFile().getAbsolutePath()));
		    }
		    else{
		    
			setFieldGrid(gritInitialiser.readGrid("G:\\gridTestFile.csv"));
			
		    }
		} catch (IOException e) {
			e.printStackTrace();
		}

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

	
	
	
	
	
	
	public GridPoint testDestination1 = new GridPoint(40, 2*GridInitialiser.getMatrixDisplacementY());
	public GridPoint testDestination2 = new GridPoint(160, 4*GridInitialiser.getMatrixDisplacementY());
	public boolean direction;
	
	
	/** Update the position based on speed and direction of the sprite */
	   public void update() {
		   
		  double x= getZeppelinMarker().getPoint().x;
		  double y= getZeppelinMarker().getPoint().y;
		  
		  if(this.direction == true){
			  
			  x+=10;
			  y+=GridInitialiser.getMatrixDisplacementY()/2;
			  
		  }
		  if(this.direction == false){
			  x-=10;
			  y-=GridInitialiser.getMatrixDisplacementY()/2;  
		  }
		  if(x<40){
			 this.direction = true;
		  }
		  if(x>120){
			  this.direction = false;
		  }
		  
		  
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
		drawZeppelinMarkers(g2d);
		//drawZeppelinMarker(g2d);
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
	
	
	public List<ZeppelinMarker> zeppelinMarkerList;
		
	public List<ZeppelinMarker> getZeppelinMarkerList() {
		return zeppelinMarkerList;
	}

	public void setZeppelinMarkerList(List<ZeppelinMarker> zeppelinMarkerList) {
		this.zeppelinMarkerList = zeppelinMarkerList;
	}

	
	public void drawZeppelinMarkers(Graphics g){
		for(ZeppelinMarker marker:zeppelinMarkerList){
			marker.drawMarker(g);	
		}
	}
	
	
	@Deprecated
	public ZeppelinMarker zeppelinMarker;
	
	@Deprecated
	public ZeppelinMarker getZeppelinMarker() {
		return zeppelinMarker;
	}

	@Deprecated
	public void setZeppelinMarker(ZeppelinMarker zeppelinMarker) {
		this.zeppelinMarker = zeppelinMarker;
	}

	@Deprecated
	public void drawZeppelinMarker(Graphics g){
		
		getZeppelinMarker().drawMarker(g);
	}
	
	
	
	
	
	
	public static void main(String[] args) {
		FieldPanel panel = new FieldPanel(0,0,1,1);
		JFrame f = new JFrame("Heart");
		f.getContentPane().add( panel, "Center" );

		f.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		f.setSize(new Dimension(450, 250));
		f.setVisible(true);
	}
}
