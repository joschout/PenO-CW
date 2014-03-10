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
	private double pixelLength;
	
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
	public Image(Mat image)
	{
		this.image = image;
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
	
	public double getPixelLength()
	{
		return this.pixelLength;
	}
	
	public void setPixelLength(double pixelLength)
	{
		this.pixelLength = pixelLength;
	}

}
