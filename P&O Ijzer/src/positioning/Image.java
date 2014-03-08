package positioning;

import java.io.File;
import java.util.List;

import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

import coordinate.GridMarker;
import coordinate.GridPoint;

/**
 * Container-klasse voor een foto.
 * @author Thomas
 *
 */
public class Image {
	
	private Mat image;
	private List<GridMarker> markers;
	
	/**
	 * Initialiseert deze image met de foto waarnaar filePath wijst.
	 * @param filePath
	 * 		Pad naar de foto, relatief tegenover de folder waarin het programma wordt uitgevoerd.
	 * 		Er wordt verwacht dat de oproeper zelf de file-extensie toevoegt.
	 * @throws IllegalArgumentException
	 * 		filePath is null.
	 * @throws IllegalArgumentException
	 * 		filePath wijst naar niet-bestaande file.
	 */
	public Image(String filePath) throws IllegalArgumentException
	{
		if (filePath == null)
		{
			throw new IllegalArgumentException("Kan image niet initialiseren met null filePath.");
		}
		File file = new File(filePath);
		if (! file.exists() || file.isDirectory())
		{
			throw new IllegalArgumentException("Kan image niet initialiseren met niet-bestaande foto.");
		}
		this.image = Highgui.imread(filePath);
		setMarkers();
	}
	
	public Mat getImage()
	{
		return this.image;
	}
	
	public double getHeight()
	{
		return this.image.size().height;
	}
	
	public double getWidth()
	{
		return this.image.size().width;
	}
	
	/**
	 * Berekent het middelpunt van de image.
	 * @return Een nieuw punt. De eerste coördinaat van het punt is de horizontale,
	 * de tweede coördinaat van het punt de verticale.
	 */
	public GridPoint getCenterCoordinatesOfImage()
	{
		double width = this.getWidth();
		double height = this.getHeight();
		return new GridPoint(width / 2, height / 2);
	}
	
	public void setMarkers() {
		ImageAnalyser imageAnalyser = new ImageAnalyser(this);
		this.markers = imageAnalyser.analysePicture();
	}
	
	public List<GridMarker> getMarkers() {
		return this.markers;
	}

}
