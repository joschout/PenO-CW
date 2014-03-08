package positioning;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import coordinate.GridMarker;
import coordinate.GridPoint;

public class ImageAnalyser {
	
	/**
	 * Analyseert de gegeven foto om de vormen met hun kleur te vinden.
	 * @param filePath
	 * 		Pad naar de foto, relatief tegenover de folder waar het programma uitgevoerd wordt.
	 * @return Een lijst met vormen met hun kleur en hun middelpunt in de foto.
	 */
	public List<GridMarker> analysePicture(String filePath)
	{
		List<GridMarker> toReturn = new ArrayList<GridMarker>();
		Image image = new Image(filePath);
		List<MatOfPoint> contours = this.calcContours(image);
		for (MatOfPoint contour: contours)
		{
			String shape = this.determineShape(contour);
			String color = this.determineColour(contour, image);
			GridPoint contourCenter = this.centerOfContour(contour);
			GridMarker marker = new GridMarker(color, shape, contourCenter);
			toReturn.add(marker);
		}
		return toReturn;
	}
	
	private List<MatOfPoint> calcContours(Image image)
	{
		Mat src = image.getImage();
		Mat dst = new Mat();
		Imgproc.Canny(src, dst, 70, 100);
		Imgproc.dilate(dst, dst, new Mat());
		List<MatOfPoint> toReturn = new ArrayList<MatOfPoint>();
		Imgproc.findContours(dst.clone(), toReturn, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_NONE);
		Iterator<MatOfPoint> it = toReturn.iterator();
		while (it.hasNext())
		{
			MatOfPoint contour = it.next();
			if (Imgproc.contourArea(contour) < 100)
				it.remove();
		}
		return toReturn;
	}
	
	private double cosine(Point pt1, Point pt2, Point pt0)
	{
	    double dx1 = pt1.x - pt0.x;
	    double dy1 = pt1.y - pt0.y;
	    double dx2 = pt2.x - pt0.x;
	    double dy2 = pt2.y - pt0.y;

	    return (dx1*dx2 + dy1*dy2)/Math.sqrt((dx1*dx1 + dy1*dy1)*(dx2*dx2 + dy2*dy2) + 1e-10);
	}
	
	private String determineShape(MatOfPoint contour)
	{
		MatOfPoint2f approx = new MatOfPoint2f();
		MatOfPoint2f contMat = new MatOfPoint2f(contour.toArray());
		Imgproc.approxPolyDP(contMat, approx, Imgproc.arcLength(contMat, true) * 0.02, true);
		Point[] approxArray = approx.toArray();

		int size = approxArray.length;
		List<Double> cos = new ArrayList<Double>();

		for (int i = 2; i <= size + 1; i++)
		{
			cos.add(cosine(approxArray[i%size], approxArray[(i-2)%size], approxArray[(i-1)%size]));
		}
		double variance = calcVariance(cos);
		double neg = countNegatives(cos);
		if (checkRectangle(cos))
		{
			return "rectangle";
		}
		else if (checkOval(cos, variance, neg))
		{
			return "oval";
		}
		else if (checkHeart(cos, variance, neg))
		{
			return "heart";
		}
		else if (checkStar(cos, variance, neg))
		{
			return "star";
		}
		else
		{
			return "undetermined";
		}	
	}
	
	private boolean checkRectangle(List<Double> cos)
	{
		int perp_count = 0;
		for (Double cosinus: cos)
		{
			if (Math.abs(cosinus) <= 0.1)
					perp_count++;
		}
		return perp_count > 2;
	}
	
	private boolean checkOval(List<Double> cos, double variance, double neg)
	{
		System.out.println(cos.size() + " " + variance);
		if ((neg / cos.size()) < 0.8)
		{
			return false;
		}
		if (variance < 0.01)
		{
			return true;
		}
		return false;
	}
	
	private boolean checkStar(List<Double> cos, double variance, double neg)
	{
		if ((neg/cos.size()) < 0.3 || (neg/cos.size()) > 0.7)
		{
			return false;
		}
		else if (variance > 0.05)
		{
			return true;
		}
		return false;
	}
	
	private boolean checkHeart(List<Double> cos, double variance, double neg)
	{
		if ((neg/cos.size()) < 0.8)
		{
			return false;
		}
		else if (variance >= 0.01)
		{
			return true;
		}
		return false;
	}
	
	private String determineColour(MatOfPoint contour, Image image)
	{
		Mat hsv = new Mat();
		Imgproc.cvtColor(image.getImage(), hsv, Imgproc.COLOR_BGR2HSV);
		Rect r = Imgproc.boundingRect(contour);
		int centerWidth = r.x + r.width / 2;
		int centerHeight = r.y + r.height / 2;
		double[] color = { 0, 0, 0 };
		double iterations = 0;
		for (int i = centerWidth - r.width / 10; i < centerWidth + r.width / 10; i++)
		{
			for (int j = centerHeight - r.height / 10; j < centerHeight + r.height / 10; j++)
			{
				double[] val = hsv.get(j, i);
				color[0] += val[0];
				color[1] += val[1];
				color[2] += val[2];
				iterations++;
			}
		}
		color[0] = (color[0] / iterations) * 2;
		color[1] = color[1] / iterations;
		color[2] = color[2] / iterations;
		return this.applyRanges(color);
	}
	
	private double calcVariance(List<Double> cos)
	{
		double mean = 0;
		for (Double cosine: cos)
		{
			mean += cosine;
		}
		mean = mean / cos.size();
		double variance = 0;
		for (Double cosine: cos)
		{
			variance += Math.pow((cosine - mean),2);
		}
		variance = variance / cos.size();
		return variance;
	}
	
	private double countNegatives(List<Double> cos)
	{
		double neg = 0;
		for (Double cosine: cos)
		{
			if (cosine < 0)
			{
				neg++;
			}
		}
		return neg;
	}
	
	private GridPoint centerOfContour(MatOfPoint contour)
	{
		Rect r = Imgproc.boundingRect(contour);
		double centerWidth = r.x + (r.width / 2);
		double centerHeight = r.y + (r.height / 2);
		return new GridPoint(centerWidth, centerHeight);
	}
	
	private String applyRanges(double[] color)
	{
		double H = color[0]; double S = color[1]; double V = color[2];
		double whiteVThreshold = 100;
		double whiteSThreshold = 40;
		if (H <= 25)
		{
			if (V >= whiteVThreshold && S <= whiteSThreshold)
			{
				return "white";
			}
			else
			{
				return "red";
			}
		}
		else if (H <= 65)
		{
			if (V >= whiteVThreshold && S <= whiteSThreshold)
			{
				return "white";
			}
			else return "yellow";
		}
		else if (H <= 180)
		{
			if (V >= whiteVThreshold && S <= whiteSThreshold)
			{
				return "white";
			}
			else return "green";
		}
		else if (H <= 260)
		{
			if (V >= whiteVThreshold && S <= whiteSThreshold)
			{
				return "white";
			}
			else return "blue";
		}
		else if (H <= 360)
		{
			if (V >= whiteVThreshold && S <= whiteSThreshold)
			{
				return "white";
			}
			else return "red";
		}
		else return "undetermined colour";
	}

}